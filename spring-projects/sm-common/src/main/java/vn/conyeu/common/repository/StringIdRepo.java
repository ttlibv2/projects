package vn.conyeu.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import vn.conyeu.common.domain.StringId;

@NoRepositoryBean
public interface StringIdRepo<E extends StringId<E>> extends DomainRepo<E, String>  {
}