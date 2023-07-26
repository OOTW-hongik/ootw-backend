package OOTWhongik.OOTW.dto.request;

import OOTWhongik.OOTW.domain.Clothes;
import lombok.Getter;

@Getter
public class ClothesUpdateRequest {
    private Long clothesId;
    private String category;
    private String subCategory;
    private String clothesComment;
    private boolean hidden;

    public Clothes toEntity() {
        return Clothes.builder()
                .id(clothesId)
                .category(category)
                .subcategory(subCategory)
                .clothesComment(clothesComment)
                .hidden(hidden)
                .build();
    }

}
