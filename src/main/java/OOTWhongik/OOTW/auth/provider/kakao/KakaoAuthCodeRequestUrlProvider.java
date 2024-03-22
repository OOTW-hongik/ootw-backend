package OOTWhongik.OOTW.auth.provider.kakao;

import OOTWhongik.OOTW.auth.provider.AuthCodeRequestUrlProvider;
import OOTWhongik.OOTW.member.domain.OauthServerType;
import OOTWhongik.OOTW.auth.properties.KakaoOauthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final KakaoOauthProperties kakaoOauthProperties;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOauthProperties.clientId())
                .queryParam("redirect_uri", kakaoOauthProperties.redirectUri())
                .queryParam("scope", String.join(",", kakaoOauthProperties.scope()))
                .toUriString();
    }
}
