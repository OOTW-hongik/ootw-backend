package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.domain.ClothesOutfit;
import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.domain.Outfit;
import OOTWhongik.OOTW.dto.request.OutfitUpdateRequest;
import OOTWhongik.OOTW.dto.response.OutfitDetailResponse;
import OOTWhongik.OOTW.dto.response.OutfitSummary;
import OOTWhongik.OOTW.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.dto.request.OutfitRequest;
import OOTWhongik.OOTW.repository.ClothesOutfitRepository;
import OOTWhongik.OOTW.repository.ClothesRepository;
import OOTWhongik.OOTW.repository.MemberRepository;
import OOTWhongik.OOTW.repository.OutfitRepository;
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

    @Transactional
    public void saveOutfit(OutfitRequest outfitRequest) {
        Member owner = memberRepository.findById(outfitRequest.getMemberId()).get();
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
    public void updateOutfit(OutfitUpdateRequest outfitUpdateRequest) {
        Outfit outfit = outfitRepository.findById(outfitUpdateRequest.getOutfitId()).get();
        outfit.setOutfitDate(outfitUpdateRequest.getOutfitDate());
        outfit.setOutfitLocation(outfitUpdateRequest.getOutfitLocation());
        outfit.setHighWc(outfitUpdateRequest.getHighWc());
        outfit.setLowWc(outfitUpdateRequest.getLowWc());
        outfit.setHighTemp(outfitUpdateRequest.getHighTemp());
        outfit.setLowTemp(outfitUpdateRequest.getLowTemp());
        outfit.setOuterRating(outfitUpdateRequest.getOuterRating());
        outfit.setTopRating(outfitUpdateRequest.getTopRating());
        outfit.setBottomRating(outfitUpdateRequest.getBottomRating());
        outfit.setOutfitComment(outfitUpdateRequest.getOutfitComment());
        outfit.setSkyCondition(outfitUpdateRequest.getSkyCondition());
        List<ClothesOutfit> clothesOutfitList = new ArrayList<>();
        List<Long> clothesList = new ArrayList<>();
        clothesList.addAll(outfitUpdateRequest.getOuterIdList());
        clothesList.addAll(outfitUpdateRequest.getTopIdList());
        clothesList.addAll(outfitUpdateRequest.getBottomIdList());
        clothesList.addAll(outfitUpdateRequest.getEtcIdList());
        for (ClothesOutfit clothesOutfit : outfit.getClothesOutfitList()) {
            if (!clothesList.contains(clothesOutfit.getClothes().getId())) {
                clothesOutfitRepository.delete(clothesOutfit);
            }
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
    }

    public List<OutfitSummary> getOutfitSummaryList(Member member) {
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
                    case "아우터":
                        cntOuter++;
                        if (mainOuter == null)
                            mainOuter = clothes;
                        break;
                    case "상의":
                        cntTop++;
                        if (mainTop == null)
                            mainTop = clothes;
                        break;
                    case "하의":
                        cntBottom++;
                        if (mainBottom == null)
                            mainBottom = clothes;
                        break;
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

    public OutfitListResponse getOutfitList(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        String name = member.getName();
        List<OutfitSummary> outfitSummaryList = getOutfitSummaryList(member);
        return new OutfitListResponse(name, outfitSummaryList);
    }

    public OutfitDetailResponse getOutfitDetail(Long outfitId) {
        Outfit outfit = outfitRepository.findById(outfitId).get();
        return OutfitDetailResponse.builder()
                .outfit(outfit)
                .build();
    }
}