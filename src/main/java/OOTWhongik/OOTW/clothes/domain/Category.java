package OOTWhongik.OOTW.clothes.domain;

import OOTWhongik.OOTW.clothes.exception.CategoryNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Category {
    OUTER("CATEGORY_OUTER", "아우터"),
    TOP("CATEGORY_TOP", "상의"),
    BOTTOM("CATEGORY_BOTTOM", "하의"),
    ETC("ETC_OUTER", "기타");

    private final String key;
    private final String name;

    public static Category findByName(String name) {
        return Arrays.stream(values())
                .filter(category -> category.name.equals(name))
                .findAny()
                .orElseThrow(() -> new CategoryNotFoundException("카테고리 입력이 옳지 않습니다."));
    }
}
