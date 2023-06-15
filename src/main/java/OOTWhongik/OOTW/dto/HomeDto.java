package OOTWhongik.OOTW.dto;

import OOTWhongik.OOTW.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HomeDto {
    private final String name;
    private final String location;

    @Builder
    public HomeDto (Member member) {
        this.name = member.getName();
        this.location = member.getLocation();
    }
}
