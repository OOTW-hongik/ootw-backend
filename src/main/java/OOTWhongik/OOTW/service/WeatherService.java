package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.dto.request.OutfitRegisterRequest;
import OOTWhongik.OOTW.dto.response.OutfitRegisterResponse;
import OOTWhongik.OOTW.httpconnection.HttpConn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherService {

    private final HttpConn httpConn;

    public static double calcWc (double temp, double velocity) {
        return 13.12 + 0.6215 * temp - 11.37 * Math.pow(velocity, 0.16) + 0.3965 * Math.pow(velocity, 0.16) * temp;
    }

    public OutfitRegisterResponse getWeatherInfo(OutfitRegisterRequest outfitRegisterRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatedNow = LocalDate.now().format(formatter);
        if (formatedNow.equals(outfitRegisterRequest.getOutfitDate().substring(0, 10))) {
            //오늘 날씨이면
            String rid = currentRidMap.get(outfitRegisterRequest.getOutfitLocation());
            int[] weatherInfo = httpConn.getWeatherI(rid);
            return OutfitRegisterResponse.builder()
                    .skyCondition(0)
                    .highTemp(weatherInfo[0])
                    .lowTemp(weatherInfo[1])
                    .highWc(weatherInfo[2])
                    .lowWc(weatherInfo[3])
                    .build();
        } else {
            // 과거 날씨이면
            String stn = pastRidMap.get(outfitRegisterRequest.getOutfitLocation());
            String tm = outfitRegisterRequest.getOutfitDate().substring(0, 4)
                    + outfitRegisterRequest.getOutfitDate().substring(5, 7)
                    + outfitRegisterRequest.getOutfitDate().substring(8, 10);
            String[] beforeInfo = httpConn.httpConnGet(tm, stn).trim().split(tm);
            String[] info = beforeInfo[1].split(",");
            for (String s : beforeInfo) {
                System.out.println(s);
            }
            int highTemp = (int) Math.round(Double.parseDouble(info[11]));
            int lowTemp = (int) Math.round(Double.parseDouble(info[13]));
            int velocity = (int) Math.round(Double.parseDouble(info[2]));
            int highWc = (int) calcWc(highTemp, velocity);
            int lowWc = (int) calcWc(lowTemp, velocity);
            return OutfitRegisterResponse.builder()
                    .skyCondition(0)
                    .highTemp(highTemp)
                    .lowTemp(lowTemp)
                    .highWc(highWc)
                    .lowWc(lowWc)
                    .build();
        }
    }

    private final Map<String, String> pastRidMap = new HashMap<>() {{
        put("서울경기","108");
        put("강원영서","212");
        put("강원영동","105");
        put("충청북도","127");
        put("충청남도","133");
        put("전라북도","146");
        put("전라남도","156");
        put("경상북도","143");
        put("경상남도","159");
        put("제주도","184");
        put("울릉독도","115");
    }};

    private final Map<String, String> currentRidMap = new HashMap<>() {{
        put("서울경기","0101010000");
        put("강원영서","0301040101");
        put("강원영동","0401020101");
        put("충청북도","0601010101");
        put("충청남도","0701010100");
        put("전라북도","0801030101");
        put("전라남도","0901010100");
        put("경상북도","1001010100");
        put("경상남도","1101010100");
        put("제주도","1301030101");
        put("울릉독도","0501010101");
    }};



}
