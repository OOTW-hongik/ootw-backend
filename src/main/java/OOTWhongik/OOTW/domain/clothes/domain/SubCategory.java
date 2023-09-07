package OOTWhongik.OOTW.domain.clothes.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubCategory {
    HOODIE("SUBCATEGORY_HOODIE", "후드"),
    SHIRTS("SUBCATEGORY_SHIRTS", "셔츠"),
    DENIM_BOTTOM("SUBCATEGORY_DENIM_BOTTOM", "청바지"),
    BLAZER("SUBCATEGORY_BLAZER", "블레이저");

    private final String key;
    private final String title;
}
