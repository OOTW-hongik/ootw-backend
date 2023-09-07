package OOTWhongik.OOTW.domain.clothes.domain;

import OOTWhongik.OOTW.domain.common.BaseTimeEntity;
import OOTWhongik.OOTW.domain.outfit.domain.Outfit;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ClothesOutfit extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clothes_outfit_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clothes_id")
    private Clothes clothes;

    @ManyToOne
    @JoinColumn(name = "outfit_id")
    private Outfit outfit;
}
