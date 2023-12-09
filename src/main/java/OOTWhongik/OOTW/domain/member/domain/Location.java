package OOTWhongik.OOTW.domain.member.domain;

import OOTWhongik.OOTW.domain.member.exception.LocationNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Location {
    SEOUL_GYEONGGI("서울경기"),
    GANGWON_YEONGSEO("강원영서"),
    GANGWON_YEONGDONG("강원영동"),
    CHUNGCHEONGBUKDO("충청북도"),
    CHUNGCHEONGNAMDO("충청남도"),
    GEOLLABUKDO("전라북도"),
    GEOLLANAMDO("전라남도"),
    GYEONGSANGBUKDO("경상북도"),
    GYEONGSANGNAMDO("경상남도"),
    JEJUDO("제주도"),
    ULLEUNG_DOKDO("울릉독도");

    private final String value;

    public static Location findByValue(String value) {
        return Arrays.stream(values())
                .filter(location -> location.value.equals(value))
                .findAny()
                .orElseThrow(() -> new LocationNotFoundException("올바르지 않은 지역이 입력되었습니다."));
    }
}
