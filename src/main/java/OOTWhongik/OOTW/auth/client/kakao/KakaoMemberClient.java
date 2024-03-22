package OOTWhongik.OOTW.auth.client.kakao;

import OOTWhongik.OOTW.auth.client.OauthMemberClient;
import OOTWhongik.OOTW.auth.properties.KakaoOauthProperties;
import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.member.domain.OauthServerType;
import OOTWhongik.OOTW.auth.dto.KakaoMemberResponse;
import OOTWhongik.OOTW.auth.dto.KakaoToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OauthMemberClient {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoOauthProperties kakaoOauthProperties;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public Member fetch(String authCode) {
        KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        KakaoMemberResponse kakaoMemberResponse =
                kakaoApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        return kakaoMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthProperties.clientId());
        params.add("redirect_uri", kakaoOauthProperties.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthProperties.clientSecret());
        return params;
    }
}
