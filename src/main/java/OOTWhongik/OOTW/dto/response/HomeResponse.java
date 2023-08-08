package OOTWhongik.OOTW.dto.response;

import OOTWhongik.OOTW.dto.WeatherGraphInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeResponse {
    private final String name;
    private final String location;
    private final int skyCondition;
    private final int highTemp;
    private final int lowTemp;
    private final int highWc;
    private final int lowWc;
    private final List<WeatherGraphInfo> weatherGraphInfoList;
    private final List<OutfitSummary> outfitSummaryList;
}
