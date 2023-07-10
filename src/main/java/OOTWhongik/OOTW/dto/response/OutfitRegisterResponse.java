package OOTWhongik.OOTW.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OutfitRegisterResponse {

    private final int skyCondition;
    private final int highWc;
    private final int lowWc;
    private final int highTemp;
    private final int lowTemp;

    @Builder
    public OutfitRegisterResponse(int skyCondition, int highWc, int lowWc, int highTemp, int lowTemp) {
        this.skyCondition = skyCondition;
        this.highWc = highWc;
        this.lowWc = lowWc;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
    }
}
