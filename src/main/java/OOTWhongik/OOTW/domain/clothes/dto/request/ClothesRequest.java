package OOTWhongik.OOTW.domain.clothes.dto.request;

import OOTWhongik.OOTW.domain.clothes.domain.Category;
import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import lombok.Getter;

@Getter
public class ClothesRequest {
    private String category;
    private String subCategory;
    private String clothesComment;
    private boolean hidden;

    public Clothes toEntity() {
        Category realCategory = Category.findByName(category);
        return Clothes.builder()
                .category(realCategory)
                .subcategory(subCategory)
                .clothesComment(clothesComment)
                .hidden(hidden)
                .build();
    }

    public Category getCategory() {
        return Category.findByName(category);
    }

}
