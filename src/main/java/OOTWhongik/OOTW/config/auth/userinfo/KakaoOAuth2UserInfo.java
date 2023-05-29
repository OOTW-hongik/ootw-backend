package OOTWhongik.OOTW.config.auth.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickname() {
        Map<String, Object> account = getKakaoAccount(attributes, "kakao_account");
        Map<String, Object> profile = getKakaoAccount(account, "profile");

        if (account == null || profile == null) {
            return null;
        }

        return (String) profile.get("nickname");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> account = getKakaoAccount(attributes, "kakao_account");
        Map<String, Object> profile = getKakaoAccount(account, "profile");

        if (account == null || profile == null) {
            return null;
        }

        return (String) profile.get("thumbnail_image_url");
    }

    private Map<String, Object> getKakaoAccount(Map<String, Object> attributes, String kakaoAccount) {
        return (Map<String, Object>) attributes.get(kakaoAccount);
    }
}