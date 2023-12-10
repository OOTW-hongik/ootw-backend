package OOTWhongik.OOTW.domain.outfit.dto.request;

import OOTWhongik.OOTW.domain.member.domain.Location;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.List;

@Getter
public class OutfitRequest {
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}\\((월|화|수|목|금|토|일)\\)$",
            message = "날짜 형식이 잘못되었습니다. 'yyyy-MM-dd(요일)' 형식으로 입력해주세요.")
    private String outfitDate;
    private String outfitLocation;
    @Min(value = 1, message = "하늘 상태값은 1 이상이어야 합니다.")
    @Max(value = 5, message = "하늘 상태값은 5 이하이어야 합니다.")
    private int skyCondition;
    @Min(value = -50, message = "체감온도는 -50 이상이어야 합니다.")
    @Max(value = 100, message = "체감온도는 100 이하이어야 합니다.")
    private int highWc;
    @Min(value = -50, message = "체감온도는 -50 이상이어야 합니다.")
    @Max(value = 100, message = "체감온도는 100 이하이어야 합니다.")
    private int lowWc;
    @Min(value = -50, message = "온도는 -50 이상이어야 합니다.")
    @Max(value = 100, message = "온도는 100 이하이어야 합니다.")
    private int highTemp;
    @Min(value = -50, message = "온도는 -50 이상이어야 합니다.")
    @Max(value = 100, message = "온도는 100 이하이어야 합니다.")
    private int lowTemp;
    @Min(value = 1, message = "평가값은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평가값은 5 이하이어야 합니다.")
    private int outerRating;
    @Min(value = 1, message = "평가값은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평가값은 5 이하이어야 합니다.")
    private int topRating;
    @Min(value = 1, message = "평가값은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평가값은 5 이하이어야 합니다.")
    private int bottomRating;
    @Min(value = 1, message = "평가값은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평가값은 5 이하이어야 합니다.")
    private int etcRating;
    private String outfitComment;
    private List<Long> outerIdList;
    private List<Long> topIdList;
    private List<Long> bottomIdList;
    private List<Long> etcIdList;

    public Location getOutfitLocation() {
        return Location.findByValue(outfitLocation);
    }


}
