package OOTWhongik.OOTW.dto;

import OOTWhongik.OOTW.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HomeResponse {
    private final String name;
    private final String location;
    private final List<OutfitResponse> outfitResponseList;

    @Builder
    public HomeResponse(Member member, List<OutfitResponse> outfitResponseList) {
        this.name = member.getName();
        this.location = member.getLocation();
        this.outfitResponseList = outfitResponseList;
    }
}
