package OOTWhongik.OOTW.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ClothesList {
    private final Long clothesId;
    private final String clothesUrl;
}