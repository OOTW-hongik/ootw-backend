package OOTWhongik.OOTW.auth.provider;

import OOTWhongik.OOTW.member.domain.OauthServerType;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}
