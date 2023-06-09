package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.domain.ClothesOutfit;
import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.domain.Outfit;
import OOTWhongik.OOTW.dto.response.OutfitResponse;
import OOTWhongik.OOTW.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.dto.request.OutfitRequest;
import OOTWhongik.OOTW.repository.ClothesRepository;
import OOTWhongik.OOTW.repository.MemberRepository;
import OOTWhongik.OOTW.repository.OutfitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OutfitService {
    private final OutfitRepository outfitRepository;
    private final MemberRepository memberRepository;
    private final ClothesRepository clothesRepository;

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
                .mainOuter(clothesRepository.findById(outfitRequest.getOuterIdList().get(0)).get())
                .mainTop(clothesRepository.findById(outfitRequest.getTopIdList().get(0)).get())
                .mainBottom(clothesRepository.findById(outfitRequest.getBottomIdList().get(0)).get())
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

    public OutfitListResponse getOutfitList(Long memberId) {
        String name = memberRepository.findById(memberId).get().getName();
        //List<Outfit> outfitList = outfitRepository.findAllByOwner(memberRepository.findById(memberId).get()).get();
        List<Outfit> outfitList = memberRepository.findById(memberId).get().getOutfitList();
        List<OutfitResponse> outfitListDtoResponse = new ArrayList<>();
        for (Outfit outfit : outfitList) {
            int cntOuter = 0;
            int cntTop = 0;
            int cntBottom = 0;
            for (ClothesOutfit clothesOutfit : outfit.getClothesOutfitList()) {
                Clothes clothes = clothesOutfit.getClothes();
                switch (clothes.getCategory()) {
                    case "아우터":
                        cntOuter++;
                        break;
                    case "상의":
                        cntTop++;
                        break;
                    case "하의":
                        cntBottom++;
                        break;
                }
            }
            OutfitResponse outfitResponseParam = OutfitResponse.builder()
                    .outfit(outfit)
                    .outerUrl(outfit.getMainOuter().getPhoto().getStoredFilePath())
                    .topUrl(outfit.getMainTop().getPhoto().getStoredFilePath())
                    .bottomUrl(outfit.getMainBottom().getPhoto().getStoredFilePath())
                    .isManyOuter(cntOuter > 1)
                    .isManyTop(cntTop > 1)
                    .isManyBottom(cntBottom > 1)
                    .build();
            outfitListDtoResponse.add(outfitResponseParam);
        }
        return new OutfitListResponse(name, outfitListDtoResponse);
    }
}