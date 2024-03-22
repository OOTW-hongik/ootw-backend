package OOTWhongik.OOTW.clothes.dto.response;

import OOTWhongik.OOTW.clothes.domain.Clothes;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ClothesResponse {
    private final Long clothesId;
    private final String clothesUrl;
    private final String subCategory;

    public static ClothesResponse from(Clothes clothes) {
        return ClothesResponse.builder()
                .clothesId(clothes.getId())
                .clothesUrl(clothes.getPhoto().getStoredFilePath())
                .subCategory(clothes.getSubcategory())
                .build();
    }
}
