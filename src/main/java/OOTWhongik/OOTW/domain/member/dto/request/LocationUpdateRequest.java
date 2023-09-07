package OOTWhongik.OOTW.domain.member.dto.request;

import lombok.Getter;

@Getter
public class LocationUpdateRequest {
    private Long memberId;
    private String location;
}
