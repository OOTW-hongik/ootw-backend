package OOTWhongik.OOTW.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Outfit extends BaseTimeEntity {
    @Id
    @Column(name = "outfit_id")
    private Long id;

    private LocalDateTime outfitDate;

    private String outfit_location; // TODO: change String to enum

    private Long highwc;
    private Long lowwc;
    private Long hightemp;
    private Long lowtemp;

    private Long outerRating;
    private Long topRating;
    private Long bottomRating;
    private Long etcRating;

    private Long skyCondition;
    private String outfitComment;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToMany(mappedBy = "outfit")
    private List<ClothesOutfit> clothesOutfitList;
}