package OOTWhongik.OOTW.dto.response;

import OOTWhongik.OOTW.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HomeResponse {
    private final String name;
    private final String location;
    private final List<OutfitSummary> outfitSummaryList;

    @Builder
    public HomeResponse(Member member, List<OutfitSummary> outfitSummaryList) {
        this.name = member.getName();
        this.location = member.getLocation();
        this.outfitSummaryList = outfitSummaryList;
    }
}
