package vn.conyeu.identity.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;
import vn.conyeu.common.repository.LongIdRepo;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.domain.AccountLink;

import java.util.Optional;

public interface AccountLinkRepo extends LongIdRepo<AccountLink> {

}