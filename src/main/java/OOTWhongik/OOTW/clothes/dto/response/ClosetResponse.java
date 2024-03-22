package OOTWhongik.OOTW.clothes.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ClosetResponse {
    private final List<String> subCategoryName;
    private final List<ClothesResponse> clothesList;

}
