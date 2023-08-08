package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.dto.WeatherGraphInfo;
import OOTWhongik.OOTW.dto.WeatherSummary;
import OOTWhongik.OOTW.dto.response.HomeResponse;
import OOTWhongik.OOTW.dto.response.OutfitSummary;
import OOTWhongik.OOTW.repository.MemberRepository;
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

    public HomeResponse getHome(Long memberId) throws IOException {
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
}
