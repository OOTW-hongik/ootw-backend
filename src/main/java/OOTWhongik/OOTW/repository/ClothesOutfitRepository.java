package OOTWhongik.OOTW.repository;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.domain.ClothesOutfit;
import OOTWhongik.OOTW.domain.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClothesOutfitRepository extends JpaRepository<ClothesOutfit, Long> {

    Optional<ClothesOutfit> findByClothesAndOutfit(Clothes clothes, Outfit outfit);

    @Modifying(clearAutomatically=true)
    @Query("DELETE FROM ClothesOutfit c WHERE c.id=:id")
    void deleteClothesOutfit(Long id);
}
