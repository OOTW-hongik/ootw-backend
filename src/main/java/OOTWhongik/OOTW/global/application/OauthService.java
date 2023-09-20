package OOTWhongik.OOTW.global.application;

import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.member.domain.OauthServerType;
import OOTWhongik.OOTW.domain.member.repository.MemberRepository;
import OOTWhongik.OOTW.global.auth.authcode.AuthCodeRequestUrlProviderComposite;
import OOTWhongik.OOTW.global.client.OauthMemberClientComposite;
import OOTWhongik.OOTW.global.config.security.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final MemberRepository memberRepository;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public String login(OauthServerType oauthServerType, String authCode) throws JsonProcessingException {
        Member member = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        Member saved = memberRepository.findByOauthId(member.getOauthId())
                .orElseGet(() -> memberRepository.save(member));
        String jws = JwtUtils.createToken(saved);
        Map<String, String> map = new HashMap<>();
        map.put("jwt", jws);
        String ret =  new ObjectMapper().writeValueAsString(map);
        return ret;
    }
}