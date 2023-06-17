package OOTWhongik.OOTW.repository;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {

    Optional<List<Clothes>> findAllByMemberAndCategory(Member member, String category);
}
