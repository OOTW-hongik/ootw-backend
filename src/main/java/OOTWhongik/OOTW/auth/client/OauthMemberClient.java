package OOTWhongik.OOTW.auth.client;

import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.member.domain.OauthServerType;

public interface OauthMemberClient {

    OauthServerType supportServer();

    Member fetch(String code);
}
