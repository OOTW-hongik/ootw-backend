package OOTWhongik.OOTW.outfit.service;

import OOTWhongik.OOTW.member.domain.Location;
import OOTWhongik.OOTW.outfit.dto.WindChillDto;
import OOTWhongik.OOTW.outfit.dto.response.WeatherGraphInfo;
import OOTWhongik.OOTW.outfit.dto.response.WeatherSummary;
import OOTWhongik.OOTW.outfit.exception.InvalidDateException;
import OOTWhongik.OOTW.outfit.exception.CrawlingException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WeatherUtil {

    @Value("${weather.url.past}")
    private String pastUrl;

    @Value("${weather.url.today}")
    private String todayUrl;

    @Value("${weather.api.auth_key}")
    private String authKey;

    public WeatherSummary getWeatherSummary(String outfitDate, Location outfitLocation) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate formattedOutfitDate = parseLocalDate(outfitDate, formatter);
        try {
            if (formattedOutfitDate.isEqual(LocalDate.now())) {
                return getTodayWeatherSummary(outfitLocation);
            } else if (formattedOutfitDate.isBefore(LocalDate.now())) {
                return getPastWeatherSummary(outfitDate, outfitLocation);
            } else {
                throw new InvalidDateException("미래의 날짜가 입력되었습니다.");
            }
        } catch (Exception ex) {
            throw new CrawlingException("날씨 정보를 가져오는데 실패했습니다.");
        }
    }

    private LocalDate parseLocalDate(String outfitDate, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(outfitDate, formatter);
        } catch (DateTimeParseException | StringIndexOutOfBoundsException ex) {
            throw new InvalidDateException("잘못된 형식의 날짜가 입력되었습니다.");
        }
    }

    public WeatherSummary getPastWeatherSummary(String outfitDate, Location outfitLocation) {
        String jijum_id = outfitLocation.getJijumId();
        String date = outfitDate.substring(0, 10);
        double highTemp;
        double lowTemp;
        int highWc;
        int lowWc;
        int skyCondition = 0;
        Document httpResponse = getCrawling(
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
        highWc = (int) Math.round(convertTempToWc(highTemp, velocity));
        lowWc = (int) Math.round(convertTempToWc(lowTemp, velocity));
        element = httpResponse.select("font").select("[color=#27385A]").get(9);
        String[] weathers = element.text().split("/");
        for (String weather : weathers) {
            skyCondition = Math.max(skyCondition, skyConditionMap.get(weather));
        }
        return WeatherSummary.builder()
                .skyCondition(skyCondition)
                .highTemp((int) Math.round(highTemp))
                .lowTemp((int) Math.round(lowTemp))
                .highWc(highWc)
                .lowWc(lowWc)
                .build();
    }

    public WeatherSummary getTodayWeatherSummary(Location outfitLocation) {
        String rid = outfitLocation.getRid();
        int highTemp;
        int lowTemp;
        int highWc;
        int lowWc;
        Document document = getCrawling(
                UriComponentsBuilder
                        .fromUriString(todayUrl)
                        .queryParam("rid", rid)
                        .toUriString()
        );
        //온도 파악
        Element element = document.select("td").select(".f11").first();
        String[] tempList = element.text().split("˚C");
        highTemp = (int) Math.round(Double.parseDouble(tempList[0].trim()));
        lowTemp = (int) Math.round(Double.parseDouble(tempList[1].trim()));
        //풍속 파악
        element = document.select("td").select("[color=\"7f7f7f\"]").first();
        String[] velocityList = element.text().split(" ");
        int velocity = Integer.parseInt(velocityList[0].trim());
        highWc = (int) Math.round(convertTempToWc(highTemp, velocity));
        lowWc = (int) Math.round(convertTempToWc(lowTemp, velocity));
        //skyCondition 파악
        element = document.select("td").select("[width=\"50%\"]").get(1);
        String html = element.html();
        String imgSrcStart = "https://api3.weatheri.co.kr/web/images/icon_2013_01/";
        int idx = html.indexOf(imgSrcStart);
        String parseString = html.substring(idx + imgSrcStart.length(), idx + imgSrcStart.length() + 2);
        int skyCondition = imgSkyConditionMap.get(Integer.parseInt(parseString));
        return WeatherSummary.builder()
                .skyCondition(skyCondition)
                .highTemp(highTemp)
                .lowTemp(lowTemp)
                .highWc(highWc)
                .lowWc(lowWc)
                .build();
    }

    public WindChillDto getWindChill(String outfitDate, Location outfitLocation) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate formattedOutfitDate;
        try {
            formattedOutfitDate = LocalDate.parse(outfitDate.substring(0, 10), formatter);
        } catch (DateTimeParseException | StringIndexOutOfBoundsException ex) {
            throw new InvalidDateException("잘못된 형식의 날짜가 입력되었습니다.");
        }
        try {
            if (formattedOutfitDate.isEqual(LocalDate.now())) {
                return getTodayWindChill(outfitLocation);
            } else if (formattedOutfitDate.isBefore(LocalDate.now())) {
                return getPastWindChill(outfitDate, outfitLocation);
            } else {
                throw new InvalidDateException("미래의 날짜가 입력되었습니다.");
            }
        } catch (Exception ex) {
            throw new CrawlingException("날씨 정보를 가져오는데 실패했습니다.");
        }
    }

    public WindChillDto getTodayWindChill(Location outfitLocation) {
        String rid = outfitLocation.getRid();
        Document httpResponse = getCrawling(
                UriComponentsBuilder
                        .fromUriString(todayUrl)
                        .queryParam("rid", rid)
                        .toUriString()
        );
        Element element1 = httpResponse.select("table").select("[bgcolor=#BCBFC2]").get(2);
        Element element2 = element1.select("tr").select("[bgcolor=#ffffff]").get(8);
        Element element3 = element1.select("tr").select("[bgcolor=#ffffff]").get(10);
        String[] temp = element2.text().trim().split(" ");
        String[] velocity = element3.text().trim().split(" ");

        try {
            return WindChillDto.builder()
                    .wcAt6((int) convertTempToWc(Double.parseDouble(temp[2]), Double.parseDouble(velocity[2])))
                    .wcAt9((int) convertTempToWc(Double.parseDouble(temp[3]), Double.parseDouble(velocity[3])))
                    .wcAt12((int) convertTempToWc(Double.parseDouble(temp[4]), Double.parseDouble(velocity[4])))
                    .wcAt15((int) convertTempToWc(Double.parseDouble(temp[5]), Double.parseDouble(velocity[5])))
                    .wcAt18((int) convertTempToWc(Double.parseDouble(temp[6]), Double.parseDouble(velocity[6])))
                    .wcAt21((int) convertTempToWc(Double.parseDouble(temp[7]), Double.parseDouble(velocity[7])))
                    .wcAt24((int) convertTempToWc(Double.parseDouble(temp[8]), Double.parseDouble(velocity[8])))
                    .build();
        } catch (IndexOutOfBoundsException ex) {
            throw new CrawlingException("오늘 체감 온도를 가져오는데 실패했습니다. 날씨 정보는 있지만 충분하지 않습니다.");
        } catch (NumberFormatException ex) {
            throw new CrawlingException("오늘 체감 온도를 가져오는데 실패했습니다. 날씨 정보가 숫자가 아닙니다.");
        }
    }

    private WindChillDto getPastWindChill(String outfitDate, Location outfitLocation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(outfitDate.substring(0, 10), formatter);
        LocalDate nextDay = date.plusDays(1);
        String uri = UriComponentsBuilder
                .fromUriString("https://apihub.kma.go.kr")
                .path("/api/typ01/url/kma_sfctm3.php")
                .queryParam("tm1", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "0600")
                .queryParam("tm2", nextDay.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "0000")
                .queryParam("stn", outfitLocation.getJijumId())
                .queryParam("authKey", authKey)
                .toUriString();
        String response = new RestTemplate().getForEntity(uri, String.class).getBody();
        String[] weathers = response.split("\n");

        return WindChillDto.builder()
                .wcAt6((int) convertTempToWc(Double.parseDouble(weathers[4].split("\\s+")[11]),
                        Double.parseDouble(weathers[4].split("\\s+")[3])))
                .wcAt9((int) convertTempToWc(Double.parseDouble(weathers[7].split("\\s+")[11]),
                        Double.parseDouble(weathers[7].split("\\s+")[3])))
                .wcAt12((int) convertTempToWc(Double.parseDouble(weathers[10].split("\\s+")[11]),
                        Double.parseDouble(weathers[10].split("\\s+")[3])))
                .wcAt15((int) convertTempToWc(Double.parseDouble(weathers[13].split("\\s+")[11]),
                        Double.parseDouble(weathers[13].split("\\s+")[3])))
                .wcAt18((int) convertTempToWc(Double.parseDouble(weathers[16].split("\\s+")[11]),
                        Double.parseDouble(weathers[16].split("\\s+")[3])))
                .wcAt21((int) convertTempToWc(Double.parseDouble(weathers[19].split("\\s+")[11]),
                        Double.parseDouble(weathers[19].split("\\s+")[3])))
                .wcAt24((int) convertTempToWc(Double.parseDouble(weathers[22].split("\\s+")[11]),
                        Double.parseDouble(weathers[22].split("\\s+")[3])))
                .build();
    }

    public List<WeatherGraphInfo> getWeatherGraphInfo(Location location) {
        String rid = location.getRid();
        Document httpResponse = getCrawling(
                UriComponentsBuilder
                        .fromUriString(todayUrl)
                        .queryParam("rid", rid)
                        .toUriString()
        );
        Element element1 = httpResponse.select("table").select("[bgcolor=#BCBFC2]").get(2);
        Element element2 = element1.select("tr").select("[bgcolor=#ffffff]").get(8);
        String[] temp = element2.text().trim().split(" ");
        if (temp.length == 0) {
            throw new CrawlingException("현재 날씨 그래프를 가져오는데 실패했습니다.");
        }
        List<WeatherGraphInfo> weatherGraphInfoList = new ArrayList<>();
        try {
            for (int i = 2; i <= 8; i++) {
                weatherGraphInfoList.add(WeatherGraphInfo.builder()
                        .time(i * 3)
                        .temp(Integer.parseInt(temp[i]))
                        .build());
            }
        } catch (IndexOutOfBoundsException ex) {
            throw new CrawlingException("현재 날씨 그래프를 가져오는데 실패했습니다. 날씨 정보는 있지만 충분하지 않습니다.");
        } catch (NumberFormatException ex) {
            throw new CrawlingException("현재 날씨 그래프를 가져오는데 실패했습니다. 날씨 정보가 숫자가 아닙니다.");
        }
        return weatherGraphInfoList;
    }

    public Document getCrawling(String url) {
        Connection conn = Jsoup.connect(url);
        try {
            return conn.get();
        } catch (IOException e) {
            throw new CrawlingException("날씨 정보를 크롤링해오는데 실패했습니다.");
        }
    }

    public static double convertTempToWc(double temp, double velocity) {
        return 13.12 + 0.6215 * temp - 11.37 * Math.pow(velocity, 0.16) + 0.3965 * Math.pow(velocity, 0.16) * temp;
    }

    /**
     * 과거 날씨에서 날씨 설명에 대응하는 skyCondition Map
     */
    private static final Map<String, Integer> skyConditionMap = new HashMap<>() {{
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
    private static final Map<Integer, Integer> imgSkyConditionMap = new HashMap<>() {{
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
