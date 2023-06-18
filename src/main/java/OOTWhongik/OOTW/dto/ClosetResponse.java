package OOTWhongik.OOTW.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ClosetResponse {
    private final List<String> subCategoryName;
    private final List<ClothesListDto> clothesListDto;

    @Builder
    public ClosetResponse(List<String> subCategoryName, List<ClothesListDto> clothesListDto) {
        this.subCategoryName = subCategoryName;
        this.clothesListDto = clothesListDto;
    }
}
