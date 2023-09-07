package OOTWhongik.OOTW.domain.clothes.dto.request;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.member.repository.MemberRepository;
import lombok.Getter;

@Getter
public class ClothesRequest {
    private Long memberId;
    private String category;
    private String subCategory;
    private String clothesComment;
    private boolean hidden;
    public Clothes toEntity(MemberRepository memberRepository) {
        if (memberRepository.findById(memberId).isPresent()) {
            return Clothes.builder()
                    .member(memberRepository.findById(memberId).get())
                    .category(category)
                    .subcategory(subCategory)
                    .clothesComment(clothesComment)
                    .hidden(hidden)
                    .build();
        }
        return null; // TODO : 예외처리 필요
    }


}
