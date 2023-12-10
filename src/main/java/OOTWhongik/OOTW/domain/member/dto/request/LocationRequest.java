package OOTWhongik.OOTW.domain.member.dto.request;

import OOTWhongik.OOTW.domain.member.domain.Location;
import jakarta.validation.constraints.NotBlank;

public class LocationRequest {

    @NotBlank(message = "지역을 입력해주세요.")
    private String location;

    public Location getLocation() {
        return Location.findByValue(location);
    }
}
