package OOTWhongik.OOTW.domain.clothes.dto.request;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import lombok.Getter;

@Getter
public class ClothesRequest {
    private String category;
    private String subCategory;
    private String clothesComment;
    private boolean hidden;
    public Clothes toEntity() {
        return Clothes.builder()
                .category(category)
                .subcategory(subCategory)
                .clothesComment(clothesComment)
                .hidden(hidden)
                .build();
    }

}
