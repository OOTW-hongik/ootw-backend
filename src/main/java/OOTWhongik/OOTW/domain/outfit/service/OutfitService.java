package OOTWhongik.OOTW.domain.outfit.service;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.clothes.domain.ClothesOutfit;
import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.outfit.domain.Outfit;
import OOTWhongik.OOTW.domain.outfit.dto.WindChillDto;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitDetailResponse;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitSummary;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.domain.outfit.dto.request.OutfitRequest;
import OOTWhongik.OOTW.domain.clothes.repository.ClothesOutfitRepository;
import OOTWhongik.OOTW.domain.clothes.repository.ClothesRepository;
import OOTWhongik.OOTW.domain.member.repository.MemberRepository;
import OOTWhongik.OOTW.domain.outfit.repository.OutfitRepository;
import OOTWhongik.OOTW.global.config.security.SecurityUtil;

import java.util.Arrays;
import java.util.Comparator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void saveOutfit(OutfitRequest outfitRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member owner = memberRepository.findById(memberId).get();
        WindChillDto windChillDto =
                weatherUtil.getWindChill(outfitRequest.getOutfitDate(), outfitRequest.getOutfitLocation());
        Outfit outfit = Outfit.builder()
                .owner(owner)
                .outfitDate(outfitRequest.getOutfitDate())
                .outfitLocation(outfitRequest.getOutfitLocation())
                .highWc(outfitRequest.getHighWc())
                .lowWc(outfitRequest.getLowWc())
                .highTemp(outfitRequest.getHighTemp())
                .lowTemp(outfitRequest.getLowTemp())
                .outerRating(outfitRequest.getOuterRating())
                .topRating(outfitRequest.getTopRating())
                .bottomRating(outfitRequest.getBottomRating())
                .etcRating(outfitRequest.getEtcRating())
                .outfitComment(outfitRequest.getOutfitComment())
                .skyCondition(outfitRequest.getSkyCondition())
                .clothesOutfitList(new ArrayList<>())
                .wcAt6(windChillDto.getWcAt6())
                .wcAt9(windChillDto.getWcAt9())
                .wcAt12(windChillDto.getWcAt12())
                .wcAt15(windChillDto.getWcAt15())
                .wcAt18(windChillDto.getWcAt18())
                .wcAt21(windChillDto.getWcAt21())
                .wcAt24(windChillDto.getWcAt24())
                .build();
        List<Long> clothesList = new ArrayList<>();
        clothesList.addAll(outfitRequest.getOuterIdList());
        clothesList.addAll(outfitRequest.getTopIdList());
        clothesList.addAll(outfitRequest.getBottomIdList());
        clothesList.addAll(outfitRequest.getEtcIdList());
        for (Long clothesId : clothesList) {
            Clothes clothes = clothesRepository.findById(clothesId).get();
            ClothesOutfit clothesOutfit = ClothesOutfit.builder()
                    .clothes(clothes)
                    .outfit(outfit)
                    .build();
            outfit.addClothesOutfit(clothesOutfit);
        }
        outfitRepository.save(outfit);
    }

    @Transactional
    public void updateOutfit(Long outfitId, OutfitRequest outfitRequest) throws Exception {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Outfit outfit = outfitRepository.findById(outfitId).get();
        if (!isOwner(member, outfit)) {
            throw new Exception("착장의 소유주가 아닙니다.");
        }
        outfit.setOutfitDate(outfitRequest.getOutfitDate());
        outfit.setOutfitLocation(outfitRequest.getOutfitLocation());
        outfit.setHighWc(outfitRequest.getHighWc());
        outfit.setLowWc(outfitRequest.getLowWc());
        outfit.setHighTemp(outfitRequest.getHighTemp());
        outfit.setLowTemp(outfitRequest.getLowTemp());
        outfit.setOuterRating(outfitRequest.getOuterRating());
        outfit.setTopRating(outfitRequest.getTopRating());
        outfit.setBottomRating(outfitRequest.getBottomRating());
        outfit.setOutfitComment(outfitRequest.getOutfitComment());
        outfit.setSkyCondition(outfitRequest.getSkyCondition());
        List<ClothesOutfit> clothesOutfitList = new ArrayList<>();
        List<Long> clothesList = new ArrayList<>();
        clothesList.addAll(outfitRequest.getOuterIdList());
        clothesList.addAll(outfitRequest.getTopIdList());
        clothesList.addAll(outfitRequest.getBottomIdList());
        clothesList.addAll(outfitRequest.getEtcIdList());
        for (ClothesOutfit clothesOutfit : outfit.getClothesOutfitList()) {
            clothesOutfitRepository.deleteClothesOutfit(clothesOutfit.getId());
        }
        for (Long clothesId : clothesList) {
            Clothes clothes = clothesRepository.findById(clothesId).get();
            Optional<ClothesOutfit> clothesOutfit = clothesOutfitRepository.findByClothesAndOutfit(clothes, outfit);
            if (clothesOutfit.isPresent()) {
                clothesOutfitList.add(clothesOutfit.get());
            } else {
                clothesOutfitList.add(ClothesOutfit.builder()
                        .clothes(clothes)
                        .outfit(outfit)
                        .build());
            }
        }
        outfit.setClothesOutfitList(clothesOutfitList);
        outfitRepository.save(outfit);
    }

    public List<OutfitSummary> getOutfitSummaryList(Member member, Optional<Integer> quantity) {
        List<Outfit> outfitList = member.getOutfitList();
        WindChillDto todayWindChill = weatherUtil.getTodayWindChill(member.getLocation());
        outfitList.sort(Comparator.comparingInt(o -> calculateWeatherDissimilarity(o, todayWindChill)));
        if (quantity.isPresent()) {
            if (quantity.get() < outfitList.size()) {
                outfitList = outfitList.subList(0, quantity.get());
            }
        } else if (outfitList.size() > 10) {
            outfitList = outfitList.subList(0, 10);
        }
        outfitList.sort(Comparator.comparingInt(o -> calculateIndicator(o, todayWindChill)));
        List<OutfitSummary> outfitSummaryList = new ArrayList<>();
        for (Outfit outfit : outfitList) {
            int cntOuter = 0;
            int cntTop = 0;
            int cntBottom = 0;
            Clothes mainOuter = null;
            Clothes mainTop = null;
            Clothes mainBottom = null;
            for (ClothesOutfit clothesOutfit : outfit.getClothesOutfitList()) {
                Clothes clothes = clothesOutfit.getClothes();
                switch (clothes.getCategory()) {
                    case OUTER -> {
                        cntOuter++;
                        if (mainOuter == null) {
                            mainOuter = clothes;
                        }
                    }
                    case TOP -> {
                        cntTop++;
                        if (mainTop == null) {
                            mainTop = clothes;
                        }
                    }
                    case BOTTOM -> {
                        cntBottom++;
                        if (mainBottom == null) {
                            mainBottom = clothes;
                        }
                    }
                }
            }
            OutfitSummary outfitSummary = OutfitSummary.builder()
                    .outfit(outfit)
                    .outerUrl(mainOuter.getPhoto().getStoredFilePath())
                    .topUrl(mainTop.getPhoto().getStoredFilePath())
                    .bottomUrl(mainBottom.getPhoto().getStoredFilePath())
                    .isManyOuter(cntOuter > 1)
                    .isManyTop(cntTop > 1)
                    .isManyBottom(cntBottom > 1)
                    .build();
            outfitSummaryList.add(outfitSummary);
        }
        return outfitSummaryList;
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
        boolean isEnd = member.getOutfitList().size() == outfitSummaryList.size();
        return new OutfitListResponse(name, isEnd, outfitSummaryList);
    }

    public OutfitDetailResponse getOutfitDetail(Long outfitId) throws Exception {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Outfit outfit = outfitRepository.findById(outfitId).get();
        if (!isOwner(member, outfit)) {
            throw new Exception("착장의 소유주가 아닙니다.");
        }
        return OutfitDetailResponse.builder()
                .outfit(outfit)
                .build();
    }

    @Transactional
    public void deleteOutfit(Long outfitId) throws Exception {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Outfit outfit = outfitRepository.findById(outfitId).get();
        if (!isOwner(member, outfit)) {
            throw new Exception("착장의 소유주가 아닙니다.");
        }
        for (ClothesOutfit clothesOutfit : outfit.getClothesOutfitList()) {
            clothesOutfitRepository.deleteClothesOutfit(clothesOutfit.getId());
        }
        outfitRepository.deleteOutfit(outfit.getId());
    }

    private boolean isOwner(Member member, Outfit outfit) {
        return outfit.getOwner() == member;
    }
}