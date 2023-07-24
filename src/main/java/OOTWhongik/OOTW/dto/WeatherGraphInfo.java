package OOTWhongik.OOTW.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WeatherGraphInfo {
    private final int time;
    private final int temp;
    private final int skycondition;

    @Builder
    public WeatherGraphInfo(int time, int temp, int skycondition) {
        this.time = time;
        this.temp = temp;
        this.skycondition = skycondition;
    }
}
