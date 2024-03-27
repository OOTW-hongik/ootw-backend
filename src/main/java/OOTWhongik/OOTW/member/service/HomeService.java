package OOTWhongik.OOTW.member.service;

import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.outfit.dto.response.WeatherGraphInfo;
import OOTWhongik.OOTW.outfit.dto.response.WeatherSummary;
import OOTWhongik.OOTW.member.dto.request.LocationRequest;
import OOTWhongik.OOTW.member.dto.response.HomeResponse;
import OOTWhongik.OOTW.outfit.dto.response.OutfitSummary;
import OOTWhongik.OOTW.member.repository.MemberRepository;
import OOTWhongik.OOTW.outfit.service.OutfitService;
import OOTWhongik.OOTW.outfit.service.WeatherUtil;
import OOTWhongik.OOTW.auth.util.SecurityUtil;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeService {
    private final OutfitService outfitService;
    private final WeatherUtil weatherUtil;
    private final MemberRepository memberRepository;

    public HomeResponse getHome() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.getReferenceById(memberId);
        WeatherSummary weatherSummary = weatherUtil.getTodayWeatherSummary(member.getLocation());
        List<WeatherGraphInfo> weatherGraphInfoList = weatherUtil.getWeatherGraphInfo(member.getLocation());
        List<OutfitSummary> outfitSummaryList = outfitService.getOutfitSummaryList(member, Optional.of(3));
        if (outfitSummaryList.size() > 3) outfitSummaryList = outfitSummaryList.subList(0, 3);
        return HomeResponse.builder()
                .name(member.getName())
                .location(member.getLocation().getRegion())
                .skyCondition(weatherSummary.getSkyCondition())
                .highTemp(weatherSummary.getHighTemp())
                .lowTemp(weatherSummary.getLowTemp())
                .highWc(weatherSummary.getHighWc())
                .lowWc(weatherSummary.getLowWc())
                .weatherGraphInfoList(weatherGraphInfoList)
                .outfitSummaryList(outfitSummaryList)
                .build();
    }

    @Transactional
    public void updateLocation(LocationRequest locationRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.getReferenceById(memberId);
        member.updateLocation(locationRequest.getLocation());
    }
}
