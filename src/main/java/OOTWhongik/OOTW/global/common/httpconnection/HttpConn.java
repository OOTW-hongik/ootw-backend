package OOTWhongik.OOTW.global.common.httpconnection;

import OOTWhongik.OOTW.global.exception.CrawlingException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpConn {

    public Document getCrawling(String url) {
        Connection conn = Jsoup.connect(url);
        try {
            return conn.get();
        } catch (IOException e) {
            throw new CrawlingException("정보를 크롤링해오는데 실패했습니다.");
        }
    }

}