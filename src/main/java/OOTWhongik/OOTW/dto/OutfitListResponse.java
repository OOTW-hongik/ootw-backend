package OOTWhongik.OOTW.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OutfitListResponse {
    private final String name;
    private final List<OutfitListDto> outfitList;

    @Builder
    public OutfitListResponse(String name, List<OutfitListDto> outfitListDtoList) {
        this.name = name;
        this.outfitList = outfitListDtoList;
    }
}
