package OOTWhongik.OOTW.domain.outfit.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WeatherSummary {
    private final int skyCondition;
    private final int highTemp;
    private final int lowTemp;
    private final int highWc;
    private final int lowWc;

    @Builder
    public WeatherSummary(int skyCondition, int highTemp, int lowTemp, int highWc, int lowWc) {
        this.skyCondition = skyCondition;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.highWc = highWc;
        this.lowWc = lowWc;
    }
}
