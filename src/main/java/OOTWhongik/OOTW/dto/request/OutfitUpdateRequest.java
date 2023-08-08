package OOTWhongik.OOTW.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OutfitUpdateRequest {
    private Long outfitId;
    private String outfitDate;
    private String outfitLocation;
    private int skyCondition;
    private int highWc;
    private int lowWc;
    private int highTemp;
    private int lowTemp;
    private int outerRating;
    private int topRating;
    private int bottomRating;
    private int etcRating;
    private String outfitComment;
    private List<Long> outerIdList;
    private List<Long> topIdList;
    private List<Long> bottomIdList;
    private List<Long> etcIdList;


}
