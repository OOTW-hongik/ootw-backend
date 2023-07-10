package OOTWhongik.OOTW.httpconnection;

import OOTWhongik.OOTW.service.WeatherService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class HttpConn {
    public String apiHubUrl = "https://apihub.kma.go.kr/api/typ01/url/kma_sfcdd.php";
    private final String apiHubKey = "ouyiGH4ASpysohh-AAqceA"; //노출 시 변경

    public String httpConnGet(String tm, String stn) {
        String sb="";

        try{
            HttpURLConnection conn=(HttpURLConnection)new URL(apiHubUrl+"?tm="+tm+"&stn="+stn+"&authKey="+apiHubKey).openConnection();

            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                sb += response.toString() + "\n";
                System.out.println(response.toString());
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        return sb;
    }

    public String httpConnPost(String endpoint, String message) {
        String sb="";

        try{
            HttpURLConnection conn=(HttpURLConnection)new URL(apiHubUrl+endpoint).openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-type", "application/json;utf-8");
            conn.setRequestProperty("Accept", "application/json");

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = message.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                sb += response.toString();
                System.out.println(response.toString());
            }

            if(sb.contains("ok")){
                return "api-axios complete";
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        return "errorPage/500";
    }

    public int[] getWeatherI (String rid) {
        final String url = "http://www.weatheri.co.kr/forecast/forecast01.php?rid=" + rid;
        System.out.println(url);
        Connection conn = Jsoup.connect(url);
        int highTemp = 0;
        int lowTemp = 0;
        int highWc = 0;
        int lowWc = 0;
        try {
            Document document = conn.get();
//            Element element = document.select("td").select("[onclick]").not("[align]").select("[style=\"cursor:pointer\"]").first();
            Element element = document.select("td").select(".f11").first();
            String[] tempList = element.text().split("˚C");
            highTemp = Integer.parseInt(tempList[0].trim());
            lowTemp = Integer.parseInt(tempList[1].trim());
            element = document.select("td").select("[color=\"7f7f7f\"]").first();
            String[] velocityList = element.text().split(" ");
            int velocity = Integer.parseInt(velocityList[0].trim());
            highWc = (int)Math.round(WeatherService.calcWc((double) highTemp, (double) velocity));
            lowWc = (int)Math.round(WeatherService.calcWc((double) lowTemp, (double) velocity));

//            Elements elements = document.select("td").select("[color=\"7f7f7f\"]");
//            int cnt = 2;
//            for (Element e : elements) {
//                if (cnt == 0) break;
//                System.out.println(e.text());
//                System.out.println();
//                cnt--;
//            }

        } catch (IOException ignored) {
        }
        return new int[] {highTemp, lowTemp, highWc, lowWc};
    }

}