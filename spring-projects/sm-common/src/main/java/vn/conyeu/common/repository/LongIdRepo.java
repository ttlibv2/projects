package vn.conyeu.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import vn.conyeu.common.domain.LongId;

@NoRepositoryBean
public interface LongIdRepo<E extends LongId<E>> extends DomainRepo<E, Long> {
}