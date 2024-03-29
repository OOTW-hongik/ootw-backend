package OOTWhongik.OOTW.clothes.dto.request;

import OOTWhongik.OOTW.clothes.domain.Category;
import OOTWhongik.OOTW.clothes.domain.Clothes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClothesRequest {
    private String category;
    @NotBlank(message = "서브 카테고리가 필요합니다.")
    private String subCategory;
    private String clothesComment;
    @NotNull(message = "옷을 숨겼는지 입력되지 않았습니다.")
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
