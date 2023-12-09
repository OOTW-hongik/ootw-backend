package OOTWhongik.OOTW.domain.clothes.repository;

import OOTWhongik.OOTW.domain.clothes.domain.Category;
import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {

    List<Clothes> findAllByMemberAndCategory(Member member, Category category);
}
