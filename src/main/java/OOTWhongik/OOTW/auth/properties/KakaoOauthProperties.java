package OOTWhongik.OOTW.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOauthProperties(
        String redirectUri,
        String clientId,
        String clientSecret,
        String[] scope
) {
}
