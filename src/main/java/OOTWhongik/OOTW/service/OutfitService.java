package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.domain.ClothesOutfit;
import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.domain.Outfit;
import OOTWhongik.OOTW.dto.OutfitListDto;
import OOTWhongik.OOTW.dto.OutfitListResponse;
import OOTWhongik.OOTW.dto.OutfitRequest;
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
                .outfit_location(outfitRequest.getOutfitLocation())
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
        for (Long clothesId : outfitRequest.getClothesList()) {
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
        List<Outfit> outfitList = outfitRepository.findAllByOwner(memberRepository.findById(memberId).get()).get();
        List<OutfitListDto> outfitListDtoList= new ArrayList<>();
        for (Outfit outfit : outfitList) {
            OutfitListDto outfitListDto = OutfitListDto.builder()
                    .outfit(outfit)
                    .outerUrl("outerUrl")
                    .topUrl("topUrl")
                    .bottomUrl("bottomUrl")
                    .isManyOuter(false)
                    .isManyTop(false)
                    .isManyBottom(false)
                    .build();
            outfitListDtoList.add(outfitListDto);
        }
        return new OutfitListResponse(name, outfitListDtoList);
    }
}