package OOTWhongik.OOTW.dto;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.repository.MemberRepository;
import lombok.Getter;

@Getter
public class ClothesRequest {
    private Long memberId;
    private String category;
    private String subCategory;
    private String clothesPhoto;
    private String clothesComment;
    private boolean hidden;

    public Clothes toEntity(MemberRepository memberRepository) {
        if (memberRepository.findById(memberId).isPresent()) {
            return Clothes.builder()
                    .member(memberRepository.findById(memberId).get())
                    .category(category)
                    .subcategory(subCategory)
                    .photoUrl(clothesPhoto)
                    .clothesComment(clothesComment)
                    .hidden(hidden)
                    .build();
        }
        return null; // TODO : 예외처리 필요
    }
}
