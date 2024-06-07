package vn.conyeu.identity.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import vn.conyeu.common.repository.LongUIdRepo;
import vn.conyeu.identity.domain.Account;
import java.util.Optional;

public interface AccountRepo extends LongUIdRepo<Account> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByPhone(String phone);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @Transactional @Modifying
    @Query("update #{#entityName} e set e.password=?2 where e.id=?1")
    void updatePassword(Long accountId, String newPassword);
}