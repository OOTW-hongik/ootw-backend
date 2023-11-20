package OOTWhongik.OOTW.domain.outfit.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OutfitListResponse {
    private final String name;
    private final boolean isEnd;
    private final List<OutfitSummary> outfitSummary;

    @Builder
    public OutfitListResponse(String name, boolean isEnd, List<OutfitSummary> outfitListDtoResponse) {
        this.name = name;
        this.isEnd = isEnd;
        this.outfitSummary = outfitListDtoResponse;
    }
}
