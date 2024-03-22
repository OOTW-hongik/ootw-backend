package OOTWhongik.OOTW.clothes.domain;

import OOTWhongik.OOTW.clothes.dto.request.ClothesRequest;
import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Clothes extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clothes_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String subcategory;

    private String clothesComment;

    private boolean hidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "clothes")
    private List<ClothesOutfit> clothesOutfitList;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    public void update(ClothesRequest clothesRequest) {
        this.category = clothesRequest.getCategory();
        this.subcategory = clothesRequest.getSubCategory();
        this.clothesComment = clothesRequest.getClothesComment();
        this.hidden = clothesRequest.isHidden();
    }

    public void addMember(Member member) {
        member.getClothesList().add(this);
        this.member = member;
    }
}
