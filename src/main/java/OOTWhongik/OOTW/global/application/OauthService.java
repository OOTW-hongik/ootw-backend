package OOTWhongik.OOTW.global.application;

import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.member.domain.OauthServerType;
import OOTWhongik.OOTW.domain.member.repository.MemberRepository;
import OOTWhongik.OOTW.global.auth.authcode.AuthCodeRequestUrlProviderComposite;
import OOTWhongik.OOTW.global.client.OauthMemberClientComposite;
import OOTWhongik.OOTW.global.config.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final MemberRepository memberRepository;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public String login(OauthServerType oauthServerType, String authCode) {
        Member member = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        Member saved = memberRepository.findByOauthId(member.getOauthId())
                .orElseGet(() -> memberRepository.save(member));
        return JwtUtils.createToken(saved);
    }
}