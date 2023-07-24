package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.domain.Outfit;
import OOTWhongik.OOTW.dto.response.HomeResponse;
import OOTWhongik.OOTW.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.dto.response.OutfitSummary;
import OOTWhongik.OOTW.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeService {
    private final OutfitService outfitService;
    private final MemberRepository memberRepository;

    public HomeResponse getHome(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        List<OutfitSummary> outfitSummaryList =  outfitService.getOutfitSummaryList(member).subList(0, 3);
        return HomeResponse.builder()
                .member(member)
                .outfitSummaryList(outfitSummaryList)
                .build();
    }
}
