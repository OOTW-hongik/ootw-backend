package OOTWhongik.OOTW.domain.member.repository;

import OOTWhongik.OOTW.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneByEmail(String email);
    Optional<Member> findById(Long id);
}
