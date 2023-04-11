package OOTWhongik.OOTW.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Clothes extends BaseTimeEntity{
    @Id
    @Column(name = "clothes_id")
    private Long id;

    private String category;

    private String subcategory;

    private String clothesComment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "clothes")
    private List<ClothesOutfit> clothesOutfitList;
}
