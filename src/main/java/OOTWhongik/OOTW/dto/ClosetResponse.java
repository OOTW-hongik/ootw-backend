package OOTWhongik.OOTW.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ClosetResponse {
    private final List<String> subCategoryName;
    private final List<ClothesResponse> clothesList;

    @Builder
    public ClosetResponse(List<String> subCategoryName, List<ClothesResponse> clothesResponse) {
        this.subCategoryName = subCategoryName;
        this.clothesList = clothesResponse;
    }
}
