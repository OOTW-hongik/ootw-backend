package OOTWhongik.OOTW.auth.service;

import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.member.domain.OauthServerType;
import OOTWhongik.OOTW.member.repository.MemberRepository;
import OOTWhongik.OOTW.auth.provider.AuthCodeRequestUrlProviderComposite;
import OOTWhongik.OOTW.auth.client.OauthMemberClientComposite;
import OOTWhongik.OOTW.auth.util.JwtUtil;
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
        String jws = JwtUtil.createToken(saved);
        Map<String, String> map = new HashMap<>();
        map.put("jwt", jws);
        return new ObjectMapper().writeValueAsString(map);
    }
}
