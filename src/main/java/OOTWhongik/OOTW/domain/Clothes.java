package OOTWhongik.OOTW.domain;

import OOTWhongik.OOTW.dto.request.ClothesUpdateRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Clothes extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clothes_id")
    private Long id;

    private String category;

    private String subcategory;

    private String clothesComment;

    private boolean hidden;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonBackReference
    @OneToMany(mappedBy = "clothes")
    private List<ClothesOutfit> clothesOutfitList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public void update(ClothesUpdateRequest clothesUpdateRequest) {
        this.category = clothesUpdateRequest.getCategory();
        this.subcategory = clothesUpdateRequest.getSubCategory();
        this.clothesComment = clothesUpdateRequest.getClothesComment();
        this.hidden = clothesUpdateRequest.isHidden();
    }
}
