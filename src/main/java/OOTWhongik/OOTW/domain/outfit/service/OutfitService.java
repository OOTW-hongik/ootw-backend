package OOTWhongik.OOTW.domain.outfit.service;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.clothes.domain.ClothesOutfit;
import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.outfit.domain.Outfit;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitDetailResponse;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitSummary;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.domain.outfit.dto.request.OutfitRequest;
import OOTWhongik.OOTW.domain.clothes.repository.ClothesOutfitRepository;
import OOTWhongik.OOTW.domain.clothes.repository.ClothesRepository;
import OOTWhongik.OOTW.domain.member.repository.MemberRepository;
import OOTWhongik.OOTW.domain.outfit.dto.response.WeatherSummary;
import OOTWhongik.OOTW.domain.outfit.repository.OutfitRepository;
import OOTWhongik.OOTW.global.config.security.SecurityUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
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
    private final WeatherService weatherService;


    @Transactional
    public void saveOutfit(OutfitRequest outfitRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member owner = memberRepository.findById(memberId).get();
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

    public List<OutfitSummary> getOutfitSummaryList(Member member) throws IOException {
        List<Outfit> outfitList = member.getOutfitList();
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
                    case "아우터" -> {
                        cntOuter++;
                        if (mainOuter == null) {
                            mainOuter = clothes;
                        }
                    }
                    case "상의" -> {
                        cntTop++;
                        if (mainTop == null) {
                            mainTop = clothes;
                        }
                    }
                    case "하의" -> {
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
        WeatherSummary todayWeather = weatherService.getTodayWeather(member.getLocation());
        outfitSummaryList.sort(((o1, o2) -> weatherDiff(o1, todayWeather) - weatherDiff(o2, todayWeather)));
        return outfitSummaryList;
    }

    private int weatherDiff(OutfitSummary outfitSummary, WeatherSummary todayWeather) {
        int highWcDiff = outfitSummary.getHighWc() - todayWeather.getHighWc();
        int lowWcDiff = outfitSummary.getLowWc() - todayWeather.getLowWc();
        int highTempDiff = outfitSummary.getHighTemp() - todayWeather.getHighTemp();
        int lowTempDiff = outfitSummary.getLowTemp() - todayWeather.getLowTemp();
        return highWcDiff * highWcDiff +
                lowWcDiff * lowWcDiff +
                highTempDiff * lowTempDiff +
                lowTempDiff * lowTempDiff;
    }

    public OutfitListResponse getOutfitList() throws IOException {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).get();
        String name = member.getName();
        List<OutfitSummary> outfitSummaryList = getOutfitSummaryList(member);
        return new OutfitListResponse(name, outfitSummaryList);
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