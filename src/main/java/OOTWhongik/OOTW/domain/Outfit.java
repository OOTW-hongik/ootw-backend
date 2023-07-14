package OOTWhongik.OOTW.domain;

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

    @OneToOne
    @JoinColumn(name = "main_outer_id")
    private Clothes mainOuter;

    @OneToOne
    @JoinColumn(name = "main_top_id")
    private Clothes mainTop;

    @OneToOne
    @JoinColumn(name = "main_bottom_id")
    private Clothes mainBottom;

    public void addClothesOutfit (ClothesOutfit clothesOutfit) {
        clothesOutfitList.add(clothesOutfit);
    }
}
