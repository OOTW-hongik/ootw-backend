package OOTWhongik.OOTW.domain.outfit.domain;

import OOTWhongik.OOTW.domain.clothes.domain.ClothesOutfit;
import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Outfit extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outfit_id")
    private Long id;

//    private LocalDateTime outfitDate;
    private String outfitDate;
    private String outfitLocation; // TODO: change String to enum

    private int highWc;
    private int lowWc;
    private int highTemp;
    private int lowTemp;

    private int outerRating;
    private int topRating;
    private int bottomRating;
    private int etcRating;

    private int skyCondition;
    private String outfitComment;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToMany(mappedBy = "outfit", cascade = CascadeType.ALL)
    @OrderColumn(name = "position")
    private List<ClothesOutfit> clothesOutfitList = new ArrayList<>();

    public void addClothesOutfit (ClothesOutfit clothesOutfit) {
        clothesOutfitList.add(clothesOutfit);
    }
    public void deleteClothesOutfit (ClothesOutfit clothesOutfit) {
        clothesOutfitList.remove(clothesOutfit);
    }
}