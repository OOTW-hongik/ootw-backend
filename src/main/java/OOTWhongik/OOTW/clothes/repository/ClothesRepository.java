package OOTWhongik.OOTW.clothes.repository;

import OOTWhongik.OOTW.clothes.domain.Category;
import OOTWhongik.OOTW.clothes.domain.Clothes;
import OOTWhongik.OOTW.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {

    List<Clothes> findAllByMemberAndCategory(Member member, Category category);
}
