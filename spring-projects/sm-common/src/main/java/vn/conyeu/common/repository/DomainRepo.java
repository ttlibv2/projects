package vn.conyeu.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import vn.conyeu.common.domain.DomainId;

import java.io.Serializable;

@NoRepositoryBean
public interface DomainRepo<E extends DomainId<E, Id>, Id extends Serializable> extends JpaRepository<E, Id>, JpaSpecificationExecutor<E> {
}