package OOTWhongik.OOTW.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ClothesOutfit extends BaseTimeEntity {
    @Id
    @Column(name = "clothes_outfit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clothes_id")
    private Clothes clothes;

    @ManyToOne
    @JoinColumn(name = "outfit_id")
    private Outfit outfit;
}
