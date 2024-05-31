package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.ts.domain.UserConfig;
import vn.conyeu.common.repository.LongIdRepo;

import java.util.List;

public interface UserConfigRepo extends LongIdRepo<UserConfig> {

    @Query("select e from #{#entityName} e where e.user.id=?1")
    List<UserConfig> loadByUserId(Long userId);
}