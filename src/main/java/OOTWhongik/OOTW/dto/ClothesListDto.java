package OOTWhongik.OOTW.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ClothesListDto {
    private final Long clothesId;
    private final String clothesUrl;
}
