package vn.conyeu.identity.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.common.repository.LongUIdRepo;
import vn.conyeu.identity.domain.AccountToken;

import java.util.Optional;

public interface AccountTokenRepo extends LongUIdRepo<AccountToken> {

    @Query("select e from #{#entityName} e where e.token=?1")
    Optional<AccountToken> findByToken(String token);
}