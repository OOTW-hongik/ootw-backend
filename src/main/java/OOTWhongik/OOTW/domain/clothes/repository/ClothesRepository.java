package OOTWhongik.OOTW.domain.clothes.repository;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {

    Optional<List<Clothes>> findAllByMemberAndCategory(Member member, String category);
}
