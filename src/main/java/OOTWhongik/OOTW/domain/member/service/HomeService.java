package OOTWhongik.OOTW.domain.member.service;

import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.outfit.dto.response.WeatherGraphInfo;
import OOTWhongik.OOTW.domain.outfit.dto.response.WeatherSummary;
import OOTWhongik.OOTW.domain.member.dto.request.LocationRequest;
import OOTWhongik.OOTW.domain.member.dto.response.HomeResponse;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitSummary;
import OOTWhongik.OOTW.domain.member.repository.MemberRepository;
import OOTWhongik.OOTW.domain.outfit.service.OutfitService;
import OOTWhongik.OOTW.domain.outfit.service.WeatherService;
import OOTWhongik.OOTW.global.config.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeService {
    private final OutfitService outfitService;
    private final WeatherService weatherService;
    private final MemberRepository memberRepository;

    public HomeResponse getHome() throws IOException {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).get();
        WeatherSummary weatherSummary = weatherService.getTodayWeather(member.getLocation());
        List<WeatherGraphInfo> weatherGraphInfoList = weatherService.getWeatherGraphInfo(member.getLocation());
        List<OutfitSummary> outfitSummaryList =  outfitService.getOutfitSummaryList(member).subList(0, 3); //TODO : 바꿔야 함
        return HomeResponse.builder()
                .name(member.getName())
                .location(member.getLocation())
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
        Member member = memberRepository.findById(memberId).get();
        member.setLocation(locationRequest.getLocation());
    }
}
