package OOTWhongik.OOTW.global.auth.config;

import OOTWhongik.OOTW.global.auth.kakao.client.KakaoApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    public KakaoApiClient kakaoApiClient() {
        return createHttpInterface(KakaoApiClient.class);
    }

    private <T> T createHttpInterface(Class<T> clazz) {
        RestClient restClient = RestClient.create();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter).build();
        return factory.createClient(clazz);
    }
}
