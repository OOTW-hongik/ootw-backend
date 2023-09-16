package OOTWhongik.OOTW.domain.outfit.service;

import OOTWhongik.OOTW.domain.outfit.dto.response.WeatherGraphInfo;
import OOTWhongik.OOTW.domain.outfit.dto.response.WeatherSummary;
import OOTWhongik.OOTW.global.common.httpconnection.HttpConn;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WeatherService {

    private final HttpConn httpConn;

    @Value("${weather.url.past}")
    private String pastUrl;

    @Value("${weather.url.today}")
    private String todayUrl;


    public static double calcWc (double temp, double velocity) {
        return 13.12 + 0.6215 * temp - 11.37 * Math.pow(velocity, 0.16) + 0.3965 * Math.pow(velocity, 0.16) * temp;
    }
    
    public WeatherSummary getWeatherInfo(String outfitDate, String outfitLocation) throws IOException {
//        if (Integer.parseInt(outfitDate.substring(0, 4)) < 1973) throw new Exception();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatedNow = LocalDate.now().format(formatter);
        System.out.println("formatedNow = " + formatedNow);
        if (formatedNow.equals(outfitDate.substring(0, 10))) {
            //오늘 날씨이면
            return getTodayWeather(outfitLocation);
        } else {
            //과거 날씨이면
            return getPastWeather(outfitDate, outfitLocation);
        }
    }

    public WeatherSummary getPastWeather(String outfitDate, String outfitLocation) throws IOException {
        String jijum_id = jijumIdMap.get(outfitLocation);
        String date = outfitDate.substring(0, 10);
        double highTemp;
        double lowTemp;
        int highWc;
        int lowWc;
        int skyCondition = 0;
        Document httpResponse = httpConn.getCrawling(
                UriComponentsBuilder
                        .fromUriString(pastUrl)
                        .queryParam("jijum_id", jijum_id)
                        .queryParam("start", date)
                        .toUriString()
        );
        Element element = httpResponse.select("tr").select("[bgcolor=#ffffff]").first();
        Elements elements = element.getAllElements();
        highTemp = Double.parseDouble(elements.get(7).text());
        lowTemp = Double.parseDouble(elements.get(9).text());
        double velocity = Double.parseDouble(elements.get(15).text());
        highWc = (int)Math.round(WeatherService.calcWc(highTemp, velocity));
        lowWc = (int)Math.round(WeatherService.calcWc(lowTemp, velocity));
        element = httpResponse.select("font").select("[color=#27385A]").get(9);
        String[] weathers = element.text().split("/");
        for (String weather : weathers) {
            skyCondition = Math.max(skyCondition, skyConditionMap.get(weather));
            System.out.println("날씨: " + weather + ", 아이콘: " + skyConditionMap.get(weather) + ", 결과: " + skyCondition);
        }
        return WeatherSummary.builder()
                .skyCondition(skyCondition)
                .highTemp((int) Math.round(highTemp))
                .lowTemp((int) Math.round(lowTemp))
                .highWc(highWc)
                .lowWc(lowWc)
                .build();
    }

    public WeatherSummary getTodayWeather(String outfitLocation) throws IOException {
        String rid = ridMap.get(outfitLocation);
        int highTemp;
        int lowTemp;
        int highWc;
        int lowWc;
        Document httpResponse = httpConn.getCrawling(
                UriComponentsBuilder
                        .fromUriString(todayUrl)
                        .queryParam("rid", rid)
                        .toUriString()
        );
        //온도 파악
        Element element = httpResponse.select("td").select(".f11").first();
        String[] tempList = element.text().split("˚C");
        highTemp = (int)Math.round(Double.parseDouble(tempList[0].trim()));
        lowTemp = (int)Math.round(Double.parseDouble(tempList[1].trim()));
        //풍속 파악
        element = httpResponse.select("td").select("[color=\"7f7f7f\"]").first();
        String[] velocityList = element.text().split(" ");
        int velocity = Integer.parseInt(velocityList[0].trim());
        highWc = (int)Math.round(WeatherService.calcWc(highTemp, velocity));
        lowWc = (int)Math.round(WeatherService.calcWc(lowTemp, velocity));
        //skyCondition 파악
        element = httpResponse.select("td").select("[width=\"50%\"]").get(1);
        String html = element.html();
        String imgSrcStart = "https://api3.weatheri.co.kr/web/images/icon_2013_01/";
        int idx = html.indexOf(imgSrcStart);
        String parseString = html.substring(idx+imgSrcStart.length(), idx+imgSrcStart.length() + 2);
        int skyCondition = imgSkyConditionMap.get(Integer.parseInt(parseString));
        return WeatherSummary.builder()
                .skyCondition(skyCondition)
                .highTemp(highTemp)
                .lowTemp(lowTemp)
                .highWc(highWc)
                .lowWc(lowWc)
                .build();
    }

    public List<WeatherGraphInfo> getWeatherGraphInfo(String location) throws IOException {
        String rid = ridMap.get(location);
        Document httpResponse = httpConn.getCrawling(
                UriComponentsBuilder
                        .fromUriString(todayUrl)
                        .queryParam("rid", rid)
                        .toUriString()
        );
        Element element1 = httpResponse.select("table").select("[bgcolor=#BCBFC2]").get(2);
        Element element2 = element1.select("tr").select("[bgcolor=#ffffff]").get(8);
        String[] temp = element2.text().trim().split(" ");

        List<WeatherGraphInfo> weatherGraphInfoList = new ArrayList<>();
        for (int i = 2; i <= 8; i++) {
            weatherGraphInfoList.add(WeatherGraphInfo.builder()
                            .time(i * 3)
                            .temp(Integer.parseInt(temp[i]))
                            .skyCondition(0)
                            .build());
        }
        return weatherGraphInfoList;
    }

    /**
     * 과거 날씨에 연관된 지역 id의 HashMap
     */
    public static final Map<String, String> jijumIdMap = new HashMap<>() {{
        put("서울경기","108"); //서울
        put("강원영서","101"); //춘천
        put("강원영동","105"); //강릉
        put("충청북도","131"); //청주
        put("충청남도","133"); //대전
        put("전라북도","146"); //전주
        put("전라남도","156"); //광주
        put("경상북도","143"); //대구
        put("경상남도","159"); //부산
        put("제주도","184"); //제주
        put("울릉독도","115"); //울릉
    }};

    /**
     * 현재 날씨에 연관된 지역 id의 HashMap
     */
    public static final Map<String, String> ridMap = new HashMap<>() {{
        put("서울경기","0101010000"); //서울
        put("강원영서","0301030101"); //춘천
        put("강원영동","0401020101"); //강릉
        put("충청북도","0601030101"); //청주
        put("충청남도","0701010100"); //대전
        put("전라북도","0801030101"); //전주
        put("전라남도","0901010100"); //광주
        put("경상북도","1001010100"); //대구
        put("경상남도","1101010100"); //부산
        put("제주도","1301030101"); //제주
        put("울릉독도","0501010101"); //울릉
    }};

    /**
     * 과거 날씨에서 날씨 설명에 대응하는 skyCondition Map
     */
    private static final Map<String ,Integer> skyConditionMap = new HashMap<>() {{
        put("맑음", 0);
        put("구름조금", 1);
        put("구름많음", 1);
        put("안개", 2);
        put("황사", 2);
        put("흐림", 2);
        put("흐림후갬", 2);
        put("약한눈", 3);
        put("눈", 3);
        put("강한눈", 3);
        put("진눈깨비", 3);
        put("소낙눈", 3);
        put("우박", 3);
        put("가끔눈", 3);
        put("눈후갬", 3);
        put("흐려져눈", 3);
        put("진눈개비", 3);
        put("약한비", 4);
        put("비", 4);
        put("강한비", 4);
        put("소나기", 4);
        put("뇌우후갬", 4);
        put("비후갬", 4);
        put("흐려져비", 4);
        put("비또는눈", 4);
        put("가끔비,눈", 4);
        put("이슬비", 4);
        put("천둥번개", 4);
    }};

    /**
     * 현재 날씨에서 날씨 이미지 번호에 대응하는 skyCondition Map
     */
    private static final Map<Integer ,Integer> imgSkyConditionMap = new HashMap<>() {{
        put(1, 0);
        put(2, 1);
        put(3, 1);
        put(4, 4);
        put(5, 3);
        put(6, 3);
        put(7, 4);
        put(8, 3);
        put(9, 2);
        put(10, 4);
        put(11, 2);
        put(12, 4);
        put(13, 4);
        put(14, 3);
        put(15, 3);
        put(16, 2);
        put(17, 4);
        put(18, 4);
        put(19, 3);
        put(20, 3);
        put(21, 1);
        put(22, 2);
    }};
}
