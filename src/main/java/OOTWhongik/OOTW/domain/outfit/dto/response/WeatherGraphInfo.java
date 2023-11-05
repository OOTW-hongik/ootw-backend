package OOTWhongik.OOTW.domain.outfit.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WeatherGraphInfo {
    private final int time;
    private final int temp;

    @Builder
    public WeatherGraphInfo(int time, int temp) {
        this.time = time;
        this.temp = temp;
    }
}
