package OOTWhongik.OOTW.repository;

import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.domain.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {
    Optional<List<Outfit>> findAllByOwner(Member owner);
}
