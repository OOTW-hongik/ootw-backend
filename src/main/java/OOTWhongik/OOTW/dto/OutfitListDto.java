package OOTWhongik.OOTW.dto;

import OOTWhongik.OOTW.domain.Outfit;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OutfitListDto {
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
    public OutfitListDto(Outfit outfit, String outerUrl, String topUrl, String bottomUrl, boolean isManyOuter, boolean isManyTop, boolean isManyBottom) {
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
}
