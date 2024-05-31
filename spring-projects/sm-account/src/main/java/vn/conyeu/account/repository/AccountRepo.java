package vn.conyeu.account.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import vn.conyeu.account.domain.Account;
import vn.conyeu.common.repository.LongIdRepo;

import java.util.Optional;

public interface AccountRepo extends LongIdRepo<Account> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByPhone(String email);

    @Transactional
    @org.springframework.data.jpa.repository.Modifying
    @Query("update #{#entityName} e set e.password=?2 where e.id=?1")
    void updatePassword(String accountId, String pwd);
}