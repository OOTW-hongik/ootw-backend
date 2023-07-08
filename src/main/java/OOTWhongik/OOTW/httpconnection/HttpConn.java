package OOTWhongik.OOTW.httpconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpConn {
    public static String apiHubUrl = "https://apihub.kma.go.kr/api/typ01/url/kma_sfcdd.php";
    private static String apiHubKey = "ouyiGH4ASpysohh-AAqceA"; //노출 시 변경

    public static String httpConnGet(String tm, String stn_ko) {
        String sb="";
        int stn = stnMap.get(stn_ko);

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

    public static String httpConnPost(String endpoint, String message) {
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

    private static final Map<String, Integer> stnMap = new HashMap<>() {{
        put("강릉",105);
        put("강진군",259);
        put("강화",201);
        put("거제",294);
        put("거창",284);
        put("경주시", 283);
        put("고산", 185);
        put("고창", 172);
        put("고창군", 251);
        put("고흥", 262);
        put("광양시", 266);
        put("광주", 156);
        put("구미", 279);
        put("군산", 140);
        put("금산", 238);
        put("김해시", 253);
        put("남원", 247);
        put("남해", 295);
        put("대관령", 100);
        put("대구", 143);
        put("대전", 133);
        put("동두천", 98);
        put("동해", 106);
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );
//        put("", );

    }};


}