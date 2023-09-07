package OOTWhongik.OOTW.domain.outfit.dto.response;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.clothes.domain.ClothesOutfit;
import OOTWhongik.OOTW.domain.outfit.domain.Outfit;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class OutfitDetailResponse {

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class UrlAndId {
        private final String clothesUrl;
        private final Long clothesId;
    }

    private final Long outfitId;
    private final String outfitDate;
    private final String outfitLocation;
    private final int skyCondition;
    private final int highWc;
    private final int lowWc;
    private final int highTemp;
    private final int lowTemp;
    private final int outerRating;
    private final int topRating;
    private final int bottomRating;
    private final int etcRating;
    private final String outfitComment;
    private final List<UrlAndId> outers;
    private final List<UrlAndId> tops;
    private final List<UrlAndId> bottoms;
    private final List<UrlAndId> etcs;

    @Builder
    public OutfitDetailResponse(Outfit outfit) {
        this.outfitId = outfit.getId();
        this.outfitDate = outfit.getOutfitDate();
        this.outfitLocation = outfit.getOutfitLocation();
        this.skyCondition = outfit.getSkyCondition();
        this.highWc = outfit.getHighWc();
        this.lowWc = outfit.getLowWc();
        this.highTemp = outfit.getHighTemp();
        this.lowTemp = outfit.getLowTemp();
        this.outerRating = outfit.getOuterRating();
        this.topRating = outfit.getTopRating();
        this.bottomRating = outfit.getBottomRating();
        this.etcRating = outfit.getEtcRating();
        this.outfitComment = outfit.getOutfitComment();
        List<UrlAndId> outers = new ArrayList<>();
        List<UrlAndId> tops = new ArrayList<>();
        List<UrlAndId> bottoms = new ArrayList<>();
        List<UrlAndId> etcs = new ArrayList<>();
        for (ClothesOutfit clothesOutfit : outfit.getClothesOutfitList()) {
            Clothes clothes = clothesOutfit.getClothes();
            UrlAndId tmp = UrlAndId.builder()
                    .clothesUrl(clothes.getPhoto().getStoredFilePath())
                    .clothesId(clothes.getId())
                    .build();
            switch (clothes.getCategory()) {
                case "아우터":
                    outers.add(tmp);
                    break;
                case "상의":
                    tops.add(tmp);
                    break;
                case "하의":
                    bottoms.add(tmp);
                    break;
                case "기타":
                    etcs.add(tmp);
                    break;
            }
        }
        this.outers = outers;
        this.tops = tops;
        this.bottoms = bottoms;
        this.etcs = etcs;
    }
}
