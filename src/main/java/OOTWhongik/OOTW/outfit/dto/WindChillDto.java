package OOTWhongik.OOTW.outfit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WindChillDto {
    private int wcAt6;
    private int wcAt9;
    private int wcAt12;
    private int wcAt15;
    private int wcAt18;
    private int wcAt21;
    private int wcAt24;
}
