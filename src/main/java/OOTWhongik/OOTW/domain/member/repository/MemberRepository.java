package OOTWhongik.OOTW.domain.member.repository;

import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.member.domain.OauthId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneByEmail(String email);
    Optional<Member> findById(Long id);
    Optional<Member> findByOauthId(OauthId oauthId);
}
