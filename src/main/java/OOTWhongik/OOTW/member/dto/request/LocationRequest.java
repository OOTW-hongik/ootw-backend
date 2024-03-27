package OOTWhongik.OOTW.member.dto.request;

import OOTWhongik.OOTW.member.domain.Location;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LocationRequest {

    @NotNull(message = "지역을 입력해주세요.")
    private Location location;
}
