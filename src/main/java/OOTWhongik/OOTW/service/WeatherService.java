package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.httpconnection.HttpConn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherService {

    public String[] getWeatherInfo(String tm, String stn_ko) {
        String[] beforeInfo = HttpConn.httpConnGet(tm, stn_ko).trim().split(tm);
        String[] info = beforeInfo[1].split(",");
        for (String s : beforeInfo) {
            System.out.println(s);
        }
        return new String[] {info[2], info[11] ,info[13]};
    }
}
