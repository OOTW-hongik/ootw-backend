package OOTWhongik.OOTW.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Name {
    private final String name;

    @Builder
    public Name(String name) {
        this.name = name;
    }
}
