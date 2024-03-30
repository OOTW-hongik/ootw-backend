package OOTWhongik.OOTW.member.repository;

import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.member.domain.OauthId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthId(OauthId oauthId);
}
