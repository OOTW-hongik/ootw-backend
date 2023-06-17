package OOTWhongik.OOTW.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Clothes extends BaseTimeEntity{
    @Id
    @Column(name = "clothes_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String subcategory;

    private String clothesComment;

    private String photoUrl; // TODO: 실제 Photos entity 랑 매핑해야 함

    private boolean hidden;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonBackReference
    @OneToMany(mappedBy = "clothes")
    private List<ClothesOutfit> clothesOutfitList;
}
