package OOTWhongik.OOTW.global.common.httpconnection;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpConn {

    public Document getCrawling (String url) throws IOException {
        Connection conn = Jsoup.connect(url);
        return conn.get();
    }

}