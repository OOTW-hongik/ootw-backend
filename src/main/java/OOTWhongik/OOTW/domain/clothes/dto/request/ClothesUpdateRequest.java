package OOTWhongik.OOTW.domain.clothes.dto.request;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.member.domain.Member;
import lombok.Getter;

@Getter
public class ClothesUpdateRequest {
    private Long clothesId;
    private String category;
    private String subCategory;
    private String clothesComment;
    private boolean hidden;

    public Clothes toEntity(Member member) {
        return Clothes.builder()
                .id(clothesId)
                .category(category)
                .subcategory(subCategory)
                .clothesComment(clothesComment)
                .hidden(hidden)
                .member(member)
                .build();
    }

}
