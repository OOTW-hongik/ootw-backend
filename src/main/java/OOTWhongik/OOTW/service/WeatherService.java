package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.dto.WeatherSummary;
import OOTWhongik.OOTW.httpconnection.HttpConn;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    public WeatherSummary getWeatherInfo(String outfitDate, String outfitLocation) throws IOException {
//        if (Integer.parseInt(outfitDate.substring(0, 4)) < 1973) throw new Exception();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatedNow = LocalDate.now().format(formatter);
        if (formatedNow.equals(outfitDate.substring(0, 10))) {
            //오늘 날씨이면
            String rid = ridMap.get(outfitLocation);
            int highTemp;
            int lowTemp;
            int highWc;
            int lowWc;
            Document httpResponse = httpConn.getCrawling(
                    "https://www.weatheri.co.kr/forecast/forecast01.php?rid=" + rid);
            Element element = httpResponse.select("td").select(".f11").first();
            String[] tempList = element.text().split("˚C");
            highTemp = (int)Math.round(Double.parseDouble(tempList[0].trim()));
            lowTemp = (int)Math.round(Double.parseDouble(tempList[1].trim()));
            element = httpResponse.select("td").select("[color=\"7f7f7f\"]").first();
            String[] velocityList = element.text().split(" ");
            int velocity = Integer.parseInt(velocityList[0].trim());
            highWc = (int)Math.round(WeatherService.calcWc(highTemp, velocity));
            lowWc = (int)Math.round(WeatherService.calcWc(lowTemp, velocity));
            return WeatherSummary.builder()
                    .skyCondition(0)
                    .highTemp(highTemp)
                    .lowTemp(lowTemp)
                    .highWc(highWc)
                    .lowWc(lowWc)
                    .build();
        } else {
            //과거 날씨이면
            String jijum_id = jijumIdMap.get(outfitLocation);
            String date = outfitDate.substring(0, 10);
            double highTemp;
            double lowTemp;
            int highWc;
            int lowWc;
            Document httpResponse = httpConn.getCrawling(
                    "https://www.weatheri.co.kr/bygone/pastDB_tmp.php?"
                            + "jijum_id=" + jijum_id
                            + "&start=" + date);
            Element element = httpResponse.select("tr").select("[bgcolor=#ffffff]").first();
            Elements elements = element.getAllElements();
            highTemp = Double.parseDouble(elements.get(7).text());
            lowTemp = Double.parseDouble(elements.get(9).text());
            double velocity = Double.parseDouble(elements.get(15).text());
            highWc = (int)Math.round(WeatherService.calcWc(highTemp, velocity));
            lowWc = (int)Math.round(WeatherService.calcWc(lowTemp, velocity));
            return WeatherSummary.builder()
                    .skyCondition(0)
                    .highTemp((int)Math.round(highTemp))
                    .lowTemp((int)Math.round(lowTemp))
                    .highWc(highWc)
                    .lowWc(lowWc)
                    .build();
        }
    }

    public static final Map<String, String> jijumIdMap = new HashMap<>() {{
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

    public static final Map<String, String> ridMap = new HashMap<>() {{
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
