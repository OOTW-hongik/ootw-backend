package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.dto.HomeDto;
import OOTWhongik.OOTW.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    final private MemberRepository memberRepository;

    public HomeDto getHome(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return HomeDto.builder()
                .member(member.get())
                .build();
    }
}
