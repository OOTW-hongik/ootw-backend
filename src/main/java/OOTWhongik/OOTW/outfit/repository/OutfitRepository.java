package OOTWhongik.OOTW.outfit.repository;

import OOTWhongik.OOTW.outfit.domain.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {
    @Modifying(clearAutomatically=true)
    @Query("DELETE FROM Outfit o WHERE o.id=:id")
    void deleteOutfit(Long id);
}
