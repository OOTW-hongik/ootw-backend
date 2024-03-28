package OOTWhongik.OOTW.outfit.service;

import OOTWhongik.OOTW.clothes.domain.Clothes;
import OOTWhongik.OOTW.clothes.domain.ClothesOutfit;
import OOTWhongik.OOTW.clothes.exception.ClothesNotFoundException;
import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.outfit.domain.Outfit;
import OOTWhongik.OOTW.outfit.dto.WindChillDto;
import OOTWhongik.OOTW.outfit.dto.response.OutfitDetailResponse;
import OOTWhongik.OOTW.outfit.dto.response.OutfitSummary;
import OOTWhongik.OOTW.outfit.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.outfit.dto.request.OutfitRequest;
import OOTWhongik.OOTW.clothes.repository.ClothesOutfitRepository;
import OOTWhongik.OOTW.clothes.repository.ClothesRepository;
import OOTWhongik.OOTW.member.repository.MemberRepository;
import OOTWhongik.OOTW.outfit.exception.OutfitNotFoundException;
import OOTWhongik.OOTW.outfit.exception.UnauthorizedOutfitAccessException;
import OOTWhongik.OOTW.outfit.repository.OutfitRepository;
import OOTWhongik.OOTW.auth.util.SecurityUtil;

import java.util.Arrays;
import java.util.Comparator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OutfitService {
    private final OutfitRepository outfitRepository;
    private final MemberRepository memberRepository;
    private final ClothesRepository clothesRepository;
    private final ClothesOutfitRepository clothesOutfitRepository;
    private final WeatherUtil weatherUtil;

    @Value("${weather.weight}")
    private double ratingWeight;

    @Transactional
    public Long saveOutfit(OutfitRequest outfitRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member owner = memberRepository.findById(memberId).get();
        WindChillDto windChillDto =
                weatherUtil.getWindChill(outfitRequest.getOutfitDate(), outfitRequest.getOutfitLocation());
        List<Clothes> clothesList = Stream.of(
                        outfitRequest.getOuterIdList(),
                        outfitRequest.getTopIdList(),
                        outfitRequest.getBottomIdList(),
                        outfitRequest.getEtcIdList())
                .flatMap(List::stream)
                .map(clothesId -> clothesRepository.findById(clothesId)
                        .orElseThrow(() -> new ClothesNotFoundException("착장을 구성하는 옷이 존재하지 않습니다.")))
                .toList();
        Outfit outfit = Outfit.createOutfit(owner, outfitRequest, windChillDto, clothesList);
        outfitRepository.save(outfit);
        return outfit.getId();
    }

    @Transactional
    public void updateOutfit(Long outfitId, OutfitRequest outfitRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new OutfitNotFoundException("id가 " + outfitId + "인 착장을 찾지 못했습니다."));
        if (!member.isOwner(outfit)) {
            throw new UnauthorizedOutfitAccessException("id가 " + outfitId + "인 착장의 소유주가 아닙니다.");
        }

        List<Clothes> clothesList = Stream.of(
                        outfitRequest.getOuterIdList(),
                        outfitRequest.getTopIdList(),
                        outfitRequest.getBottomIdList(),
                        outfitRequest.getEtcIdList())
                .flatMap(List::stream)
                .map(clothesId -> clothesRepository.findById(clothesId)
                        .orElseThrow(() -> new ClothesNotFoundException("착장을 구성하는 옷이 존재하지 않습니다.")))
                .toList();
        for (ClothesOutfit clothesOutfit : outfit.getClothesOutfitList()) {
            clothesOutfitRepository.deleteClothesOutfit(clothesOutfit.getId());
        }
        outfit.update(outfitRequest, clothesList);
        outfitRepository.save(outfit);
    }

    public List<OutfitSummary> getOutfitSummaryList(Member member, Optional<Integer> quantity) {
        List<Outfit> outfitList = member.getOutfitList();
        WindChillDto todayWindChill = weatherUtil.getTodayWindChill(member.getLocation());
        outfitList.sort(Comparator.comparingInt(o -> calculateWeatherDissimilarity(o, todayWindChill)));
        if (quantity.isPresent() && quantity.get() < outfitList.size()) {
            outfitList = outfitList.subList(0, quantity.get());
        } else if (outfitList.size() > 10) {
            outfitList = outfitList.subList(0, 10);
        }
        outfitList.sort(Comparator.comparingInt(o -> calculateIndicator(o, todayWindChill)));
        return outfitList.stream()
                .map(OutfitSummary::of)
                .collect(Collectors.toList());
    }

    private int calculateIndicator(Outfit outfit, WindChillDto todayWindChill) {
        int weatherDissimilarity = calculateWeatherDissimilarity(outfit, todayWindChill);
        double ratingDeviation = calculateRatingDeviation(outfit);
        return (int) (weatherDissimilarity + ratingDeviation);
    }

    private static int calculateWeatherDissimilarity(Outfit outfit, WindChillDto todayWindChill) {
        int[] windChillDiff = {
                outfit.getWcAt6() - todayWindChill.getWcAt6(),
                outfit.getWcAt9() - todayWindChill.getWcAt9(),
                outfit.getWcAt12() - todayWindChill.getWcAt12(),
                outfit.getWcAt15() - todayWindChill.getWcAt15(),
                outfit.getWcAt18() - todayWindChill.getWcAt18(),
                outfit.getWcAt21() - todayWindChill.getWcAt21(),
                outfit.getWcAt24() - todayWindChill.getWcAt24()
        };
        return Arrays.stream(windChillDiff).map(wc -> wc * wc).sum();
    }

    private double calculateRatingDeviation(Outfit outfit) {
        return ratingWeight *
                ((outfit.getOuterRating() - 3) * (outfit.getOuterRating() - 3)
                        + (outfit.getTopRating() - 3) * (outfit.getTopRating() - 3)
                        + (outfit.getBottomRating() - 3) * (outfit.getBottomRating() - 3));
    }

    public OutfitListResponse getOutfitList(Optional<Integer> quantity) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).get();
        String name = member.getName();
        List<OutfitSummary> outfitSummaryList = getOutfitSummaryList(member, quantity);
        boolean isEnd = member.getOutfitList().size() >= outfitSummaryList.size();
        return new OutfitListResponse(name, isEnd, outfitSummaryList);
    }

    public OutfitDetailResponse getOutfitDetail(Long outfitId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new OutfitNotFoundException("id가 " + outfitId + "인 착장을 찾지 못했습니다."));
        if (!member.isOwner(outfit)) {
            throw new UnauthorizedOutfitAccessException("id가 " + outfitId + "인 착장의 소유주가 아닙니다.");
        }
        return OutfitDetailResponse.builder()
                .outfit(outfit)
                .build();
    }

    @Transactional
    public void deleteOutfit(Long outfitId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new OutfitNotFoundException("id가 " + outfitId + "인 착장을 찾지 못했습니다."));
        if (!member.isOwner(outfit)) {
            throw new UnauthorizedOutfitAccessException("id가 " + outfitId + "인 착장의 소유주가 아닙니다.");
        }
        outfit.getClothesOutfitList().stream()
                .map(ClothesOutfit::getId)
                .forEach(clothesOutfitRepository::deleteClothesOutfit);
        outfitRepository.deleteOutfit(outfit.getId());
    }
}
