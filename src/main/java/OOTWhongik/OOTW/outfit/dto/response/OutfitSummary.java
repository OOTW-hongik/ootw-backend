package OOTWhongik.OOTW.outfit.dto.response;

import OOTWhongik.OOTW.clothes.domain.Clothes;
import OOTWhongik.OOTW.clothes.domain.ClothesOutfit;
import OOTWhongik.OOTW.outfit.domain.Outfit;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OutfitSummary {
    private final Long outfitId;
    private final String outfitDate;
    private final int skyCondition;
    private final int highWc;
    private final int lowWc;
    private final int highTemp;
    private final int lowTemp;
    private final int outerRating;
    private final int topRating;
    private final int bottomRating;
    private final String outerUrl;
    private final String topUrl;
    private final String bottomUrl;
    private final boolean isManyOuter;
    private final boolean isManyTop;
    private final boolean isManyBottom;

    @Builder
    public OutfitSummary(Outfit outfit, String outerUrl, String topUrl, String bottomUrl,
                         boolean isManyOuter, boolean isManyTop, boolean isManyBottom) {
        this.outfitId = outfit.getId();
        this.outfitDate = outfit.getOutfitDate();
        this.skyCondition = outfit.getSkyCondition();
        this.highWc = outfit.getHighWc();
        this.lowWc = outfit.getLowWc();
        this.highTemp = outfit.getHighTemp();
        this.lowTemp = outfit.getLowTemp();
        this.outerRating = outfit.getOuterRating();
        this.topRating = outfit.getTopRating();
        this.bottomRating = outfit.getBottomRating();
        this.outerUrl = outerUrl;
        this.topUrl = topUrl;
        this.bottomUrl = bottomUrl;
        this.isManyOuter = isManyOuter;
        this.isManyTop = isManyTop;
        this.isManyBottom = isManyBottom;
    }

    public static OutfitSummary of(Outfit outfit) {
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
        return OutfitSummary.builder()
                .outfit(outfit)
                .outerUrl(mainOuter.getPhoto().getStoredFilePath())
                .topUrl(mainTop.getPhoto().getStoredFilePath())
                .bottomUrl(mainBottom.getPhoto().getStoredFilePath())
                .isManyOuter(cntOuter > 1)
                .isManyTop(cntTop > 1)
                .isManyBottom(cntBottom > 1)
                .build();
    }
}
