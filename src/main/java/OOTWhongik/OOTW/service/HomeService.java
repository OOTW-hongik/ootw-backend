package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.domain.Outfit;
import OOTWhongik.OOTW.dto.HomeResponse;
import OOTWhongik.OOTW.dto.OutfitResponse;
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
    final private MemberRepository memberRepository;

    public HomeResponse getHome(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        List<Outfit> outfitList = member.getOutfitList();
        List<OutfitResponse> outfitResponseList = new ArrayList<>();
        int count = 0;
        for (Outfit outfit : outfitList) {
            OutfitResponse outfitResponse = OutfitResponse.builder()
                    .outfit(outfit)
                    .outerUrl("outerUrl")
                    .topUrl("topUrl")
                    .bottomUrl("bottomUrl")
                    .isManyOuter(false)
                    .isManyTop(false)
                    .isManyBottom(false)
                    .build();
            outfitResponseList.add(outfitResponse);
            count++;
            if (count >= 3) break;
        }

        return HomeResponse.builder()
                .member(member)
                .outfitResponseList(outfitResponseList)
                .build();
    }
}
