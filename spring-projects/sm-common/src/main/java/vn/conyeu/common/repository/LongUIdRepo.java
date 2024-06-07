package vn.conyeu.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import vn.conyeu.common.domain.LongUId;

@NoRepositoryBean
public interface LongUIdRepo<E extends LongUId<E>> extends DomainRepo<E, Long> {
}