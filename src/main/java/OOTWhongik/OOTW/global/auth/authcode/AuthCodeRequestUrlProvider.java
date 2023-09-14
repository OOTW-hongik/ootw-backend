package OOTWhongik.OOTW.global.auth.authcode;

import OOTWhongik.OOTW.domain.member.domain.OauthServerType;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}