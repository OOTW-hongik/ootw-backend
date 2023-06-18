package OOTWhongik.OOTW.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OutfitListResponse {
    private final String name;
    private final List<OutfitResponse> outfitResponse;

    @Builder
    public OutfitListResponse(String name, List<OutfitResponse> outfitListDtoResponse) {
        this.name = name;
        this.outfitResponse = outfitListDtoResponse;
    }
}
