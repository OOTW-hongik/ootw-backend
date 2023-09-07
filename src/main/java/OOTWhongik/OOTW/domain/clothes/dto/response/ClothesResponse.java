package OOTWhongik.OOTW.domain.clothes.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ClothesResponse {
    private final Long clothesId;
    private final String clothesUrl;
    private final String subCategory;
}
