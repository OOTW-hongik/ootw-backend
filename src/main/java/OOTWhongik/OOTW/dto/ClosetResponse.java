package OOTWhongik.OOTW.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ClosetResponse {
    private final List<String> subCategoryName;
    private final List<ClothesList> clothesList;

    @Builder
    public ClosetResponse(List<String> subCategoryName, List<ClothesList> clothesList) {
        this.subCategoryName = subCategoryName;
        this.clothesList = clothesList;
    }
}
