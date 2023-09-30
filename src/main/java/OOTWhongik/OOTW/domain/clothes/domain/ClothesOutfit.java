package OOTWhongik.OOTW.domain.clothes.domain;

import OOTWhongik.OOTW.domain.outfit.domain.Outfit;
import OOTWhongik.OOTW.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id")
    private Clothes clothes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outfit_id")
    private Outfit outfit;
}
