package OOTWhongik.OOTW.global.client;

import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.member.domain.OauthServerType;

public interface OauthMemberClient {

    OauthServerType supportServer();

    Member fetch(String code);
}