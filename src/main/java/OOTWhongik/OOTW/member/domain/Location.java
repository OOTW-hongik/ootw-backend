package OOTWhongik.OOTW.member.domain;

import OOTWhongik.OOTW.member.exception.LocationNotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Location {

    SEOUL_GYEONGGI("서울경기", "서울", "108", "0101010000"),
    GANGWON_YEONGSEO("강원영서", "춘천", "101", "0301030101"),
    GANGWON_YEONGDONG("강원영동", "강릉", "105", "0401020101"),
    CHUNGCHEONGBUKDO("충청북도", "청주", "131", "0601030101"),
    CHUNGCHEONGNAMDO("충청남도", "대전", "133", "0701010100"),
    JEOLLABUKDO("전라북도", "전주", "146", "0801030101"),
    JEOLLANAMDO("전라남도", "광주", "156", "0901010100"),
    GYEONGSANGBUKDO("경상북도", "대구", "143", "1001010100"),
    GYEONGSANGNAMDO("경상남도", "부산", "159", "1101010100"),
    JEJUDO("제주도", "제주", "184", "1301030101"),
    ULLUNG_DOKDO("울릉독도", "울릉", "115", "0501010101"),
    ;

    private final String region;
    private final String regionInWeatherI;
    private final String jijumId;
    private final String rid;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Location from(@JsonProperty("location") String region) {
        return Arrays.stream(values())
                .filter(location -> location.region.equals(region))
                .findAny()
                .orElseThrow(() -> new LocationNotFoundException("올바르지 않은 지역이 입력되었습니다."));
    }
}
