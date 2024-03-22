package OOTWhongik.OOTW.outfit.domain;

import OOTWhongik.OOTW.clothes.domain.Clothes;
import OOTWhongik.OOTW.clothes.domain.ClothesOutfit;
import OOTWhongik.OOTW.member.domain.Location;
import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.outfit.dto.WindChillDto;
import OOTWhongik.OOTW.outfit.dto.request.OutfitRequest;
import OOTWhongik.OOTW.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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

    private String outfitDate;
    @Enumerated(EnumType.STRING)
    private Location outfitLocation;

    private int wcAt6;
    private int wcAt9;
    private int wcAt12;
    private int wcAt15;
    private int wcAt18;
    private int wcAt21;
    private int wcAt24;

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
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToMany(mappedBy = "outfit", cascade = CascadeType.ALL)
    @OrderColumn(name = "position")
    private List<ClothesOutfit> clothesOutfitList;

    public static Outfit createOutfit(Member owner, OutfitRequest outfitRequest,
                                      WindChillDto windChillDto, List<Clothes> clothesList) {
        Outfit outfit =  Outfit.builder()
                .owner(owner)
                .outfitDate(outfitRequest.getOutfitDate())
                .outfitLocation(outfitRequest.getOutfitLocation())
                .highWc(outfitRequest.getHighWc())
                .lowWc(outfitRequest.getLowWc())
                .highTemp(outfitRequest.getHighTemp())
                .lowTemp(outfitRequest.getLowTemp())
                .outerRating(outfitRequest.getOuterRating())
                .topRating(outfitRequest.getTopRating())
                .bottomRating(outfitRequest.getBottomRating())
                .etcRating(outfitRequest.getEtcRating())
                .outfitComment(outfitRequest.getOutfitComment())
                .skyCondition(outfitRequest.getSkyCondition())
                .clothesOutfitList(new ArrayList<>())
                .wcAt6(windChillDto.getWcAt6())
                .wcAt9(windChillDto.getWcAt9())
                .wcAt12(windChillDto.getWcAt12())
                .wcAt15(windChillDto.getWcAt15())
                .wcAt18(windChillDto.getWcAt18())
                .wcAt21(windChillDto.getWcAt21())
                .wcAt24(windChillDto.getWcAt24())
                .build();
        for (Clothes clothes : clothesList) {
            ClothesOutfit clothesOutfit = ClothesOutfit.builder()
                    .clothes(clothes)
                    .outfit(outfit)
                    .build();
            outfit.addClothesOutfit(clothesOutfit);
        }
        return outfit;
    }

    public void addClothesOutfit (ClothesOutfit clothesOutfit) {
        clothesOutfitList.add(clothesOutfit);
    }

    public void update(OutfitRequest outfitRequest, List<Clothes> clothesList) {
        this.outfitDate = outfitRequest.getOutfitDate();
        this.outfitLocation = outfitRequest.getOutfitLocation();
        this.highWc = outfitRequest.getHighWc();
        this.lowWc = outfitRequest.getLowWc();
        this.highTemp = outfitRequest.getHighTemp();
        this.lowTemp = outfitRequest.getLowTemp();
        this.outerRating = outfitRequest.getOuterRating();
        this.topRating = outfitRequest.getTopRating();
        this.bottomRating = outfitRequest.getBottomRating();
        this.outfitComment = outfitRequest.getOutfitComment();
        this.skyCondition = outfitRequest.getSkyCondition();
        this.clothesOutfitList = new ArrayList<>();
        for (Clothes clothes : clothesList) {
            clothesOutfitList.add(ClothesOutfit.builder()
                    .clothes(clothes)
                    .outfit(this)
                    .build());
        }
    }
}
