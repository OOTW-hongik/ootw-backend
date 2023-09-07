package OOTWhongik.OOTW.domain.outfit.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OutfitListResponse {
    private final String name;
    private final List<OutfitSummary> outfitSummary;

    @Builder
    public OutfitListResponse(String name, List<OutfitSummary> outfitListDtoResponse) {
        this.name = name;
        this.outfitSummary = outfitListDtoResponse;
    }
}
