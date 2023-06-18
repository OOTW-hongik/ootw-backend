package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.dto.HomeResponse;
import OOTWhongik.OOTW.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    final private MemberRepository memberRepository;

    public HomeResponse getHome(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return HomeResponse.builder()
                .member(member.get())
                .build();
    }
}
