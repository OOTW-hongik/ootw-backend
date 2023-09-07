package OOTWhongik.OOTW.domain.clothes.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    OUTER("CATEGORY_OUTER", "아우터"),
    TOP("CATEGORY_TOP", "상의"),
    BOTTOM("CATEGORY_BOTTOM", "하의"),
    ETC("ETC_OUTER", "기타");

    private final String key;
    private final String title;
}
