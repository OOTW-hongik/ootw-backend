package OOTWhongik.OOTW.repository;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.domain.ClothesOutfit;
import OOTWhongik.OOTW.domain.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClothesOutfitRepository extends JpaRepository<ClothesOutfit, Long> {

    Optional<ClothesOutfit> findByClothesAndOutfit(Clothes clothes, Outfit outfit);
}
