package OOTWhongik.OOTW.domain.clothes.domain;

import OOTWhongik.OOTW.domain.clothes.dto.request.ClothesRequest;
import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
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
    private List<ClothesOutfit> clothesOutfitList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

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
