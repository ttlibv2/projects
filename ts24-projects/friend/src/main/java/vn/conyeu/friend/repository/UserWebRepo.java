package vn.conyeu.friend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.conyeu.friend.domain.UserWeb;
import vn.conyeu.common.repository.LongIdRepo;

import java.util.Optional;

public interface UserWebRepo extends LongIdRepo<UserWeb> {
    Optional<UserWeb> findByWebId(String webId);

    @Query(value = "select e from #{#entityName} e where e.webId like '?1::'",
    countQuery = "select count(e) from #{#entityName} e where e.webId like '?1::'")
    Page<UserWeb> findByWeb(String domain, Pageable pageable);

    boolean existsByWebId(String webId);
}