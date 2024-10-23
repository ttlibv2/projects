package vn.conyeu.common.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import vn.conyeu.common.domain.DomainId;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.common.repository.DomainRepo;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public abstract class DomainService<S extends DomainId<S, Id>, Id extends Serializable, R extends DomainRepo<S, Id>> {
    protected ICacheService cacheService = ICacheService.defaultService();

    private final R entityRepo;

    public DomainService(R domainRepo) {
        this.entityRepo = domainRepo;
    }

    @Autowired
    protected void setCacheService(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    public BaseException noId(Id id) {
        return new NotFound().code("noId").arguments("id", id)
                .message("Không tồn tại dữ liệu %s", id);
    }

    protected final R repo() {
        return entityRepo;
    }

    /**
     * Returns a single entity matching the given {@link Example} or {@link Optional#empty()} if none was found.
     *
     * @param example must not be {@literal null}.
     * @return a single entity matching the given {@link Example} or {@link Optional#empty()} if none was found.
     * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if the Example yields more than one result.
     */
    public Optional<S> findOne(Example<S> example) {
        return entityRepo.findOne(example);
    }

    /**
     * Returns a single entity matching the given {@link Specification} or {@link Optional#empty()} if none found.
     *
     * @param spec must not be {@literal null}.
     * @return never {@literal null}.
     * @throws IncorrectResultSizeDataAccessException if more than one entity found.
     */
    public Optional<S> findOne(Specification<S> spec) {
        return entityRepo.findOne(spec);
    }

    /**
     * Returns all entities matching the given {@link Specification}.
     *
     * @param spec must not be {@literal null}.
     * @return never {@literal null}.
     */
    public List<S> findAll(Specification<S> spec) {
        return entityRepo.findAll(spec);
    }

    /**
     * Returns a {@link Page} of entities matching the given {@link Specification}.
     *
     * @param spec     must not be {@literal null}.
     * @param pageable must not be {@literal null}.
     * @return never {@literal null}.
     */
    public Page<S> findAll(Specification<S> spec, Pageable pageable) {
        return entityRepo.findAll(spec, pageable);
    }

    /**
     * Returns all entities matching the given {@link Specification} and {@link Sort}.
     *
     * @param spec must not be {@literal null}.
     * @param sort must not be {@literal null}.
     * @return never {@literal null}.
     */
    public List<S> findAll(Specification<S> spec, Sort sort) {
        return entityRepo.findAll(spec, sort);
    }

    /**
     * Returns the number of instances that the given {@link Specification} will return.
     *
     * @param spec the {@link Specification} to count instances for, must not be {@literal null}.
     * @return the number of instances.
     */
    public long count(Specification<S> spec) {
        return entityRepo.count(spec);
    }

    /**
     * Checks whether the data store contains elements that match the given {@link Specification}.
     *
     * @param spec the {@link Specification} to use for the existence check, ust not be {@literal null}.
     * @return {@code true} if the data store contains elements that match the given {@link Specification} otherwise
     * {@code false}.
     */
    public boolean exists(Specification<S> spec) {
        return entityRepo.exists(spec);
    }

    /**
     * Deletes by the {@link Specification} and returns the number of rows deleted.
     * <p>
     * This method uses {@link CriteriaDelete Criteria API bulk delete} that maps directly to
     * database delete operations. The persistence context is not synchronized with the result of the bulk delete.
     * <p>
     * Please note that {@link CriteriaQuery} in,
     * {@link Specification#toPredicate(jakarta.persistence.criteria.Root, CriteriaQuery, CriteriaBuilder)} will be {@literal null} because
     * {@link CriteriaBuilder#createCriteriaDelete(Class)} does not implement
     * {@code CriteriaQuery}.
     *
     * @param spec the {@link Specification} to use for the existence check, must not be {@literal null}.
     * @return the number of entities deleted.
     * @since 3.0
     */
    public long delete(Specification<S> spec) {
        return entityRepo.delete(spec);
    }

    /**
     * Returns entities matching the given {@link Specification} applying the {@code queryFunction} that defines the query
     * and its result type.
     *
     * @param spec          must not be null.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @return all entities matching the given Example.
     * @since 3.0
     */
    public <S1 extends S, R> R findBy(Specification<S> spec, Function<FluentQuery.FetchableFluentQuery<S1>, R> queryFunction) {
        return entityRepo.findBy(spec, queryFunction);
    }

    /**
     * Returns all entities matching the given {@link Example}. In case no match could be found an empty {@link Iterable}
     * is returned.
     *
     * @param search must not be {@literal null}.
     * @return all entities matching the given {@link Example}.
     */
    public List<S> findAll(Example<S> search) {
        return entityRepo.findAll(search);
    }

    /**
     * Returns all entities matching the given {@link Example} applying the given {@link Sort}. In case no match could be
     * found an empty {@link Iterable} is returned.
     *
     * @param search must not be {@literal null}.
     * @param sort   the {@link Sort} specification to sort the results by, may be {@link Sort#unsorted()}, must not be
     *               {@literal null}.
     * @return all entities matching the given {@link Example}.
     */
    public List<S> findAll(Example<S> search, Sort sort) {
        return entityRepo.findAll(search, sort);
    }

    /**
     * Returns a {@link Page} of entities matching the given {@link Example}. In case no match could be found, an empty
     * {@link Page} is returned.
     *
     * @param search   must not be {@literal null}.
     * @param pageable the pageable to request a paged result, can be {@link Pageable#unpaged()}, must not be
     *                 {@literal null}.
     * @return a {@link Page} of entities matching the given {@link Example}.
     */
    public Page<S> findAll(Example<S> search, Pageable pageable) {
        return entityRepo.findAll(search, pageable);
    }

    public List<S> findAll() {
        return entityRepo.findAll();
    }

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@link Pageable} object.
     *
     * @param pageable the pageable to request a paged result, can be {@link Pageable#unpaged()}, must not be
     *                 {@literal null}.
     * @return a page of entities
     */
    public Page<S> findAll(Pageable pageable) {
        return entityRepo.findAll(pageable);
    }

    public Page<S> findAll(ObjectMap search, Pageable pageable) {
        return entityRepo.findAll(pageable);
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param entityId must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    public Optional<S> findById(Id entityId) {
        return entityRepo.findById(entityId);
    }

    public final S getById(Id entityId) {
        return findById(entityId).orElseThrow(()->noId(entityId));
    }

    /**
     * Returns all instances of the type {@code T} with the given IDs.
     * <p>
     * If some or all ids are not found, no entities are returned for these IDs.
     * <p>
     * Note that the order of elements in the result is not guaranteed.
     *
     * @param ids must not be {@literal null} nor contain any {@literal null} values.
     * @return guaranteed to be not {@literal null}. The size can be equal or less than the number of given
     *         {@literal ids}.
     * @throws IllegalArgumentException in case the given {@link Iterable ids} or one of its items is {@literal null}.
     */
    public List<S> findAllById(Iterable<Id> ids) {
        return entityRepo.findAllById(ids);
    }

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param entityId must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    public boolean existsById(Id entityId) {
        return entityRepo.existsById(entityId);
    }

    /**
     * Checks whether the data store contains elements that match the given {@link Example}.
     *
     * @param example the {@link Example} to use for the existence check. Must not be {@literal null}.
     * @return {@literal true} if the data store contains elements that match the given {@link Example}.
     */
    public boolean exists(Example<S> example) {
        return entityRepo.exists(example);
    }

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities.
     */
    public long count() {
        return entityRepo.count();
    }

    /**
     * Returns the number of instances matching the given {@link Example}.
     *
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return the number of instances matching the {@link Example}.
     */
    public long count(Example<S> example) {
        return entityRepo.count(example);
    }

    /**
     * Deletes the entity with the given id.
     *
     * @param entityId must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    public void deleteById(Id entityId) {
        entityRepo.deleteById(entityId);
    }

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    public void delete(S entity) {
        entityRepo.delete(entity);
    }

    /**
     * Deletes all entities managed by the repository.
     */
    public void deleteAll() {
        entityRepo.deleteAll();
    }

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     */
    @Transactional
    public S createNew(S entity) {
        return saveAndReturn(entity);
    }

    /**
     * Saves an entity and flushes changes instantly.
     *
     * @param entity entity to be saved. Must not be {@literal null}.
     * @return the saved entity
     */
    public <S1 extends S> S1 saveAndFlush(S1 entity) {
        return entityRepo.saveAndFlush(entity);
    }
    /**
     * Saves all entities and flushes changes instantly.
     *
     * @param entities entities to be saved. Must not be {@literal null}.
     * @return the saved entities
     * @since 2.5
     */
    public <S1 extends S> List<S1> saveAllAndFlush(Iterable<S1> entities) {
        return entityRepo.saveAllAndFlush(entities);
    }

    public List<S> saveAll(Iterable<S> entities) {
        return entityRepo.saveAll(entities);
    }

    public List<S> saveAll(Collection<S> entities) {
        if(entities == null || entities.isEmpty()) return new LinkedList<>();
        return entityRepo.saveAll(entities);
    }

    @Transactional
    public S saveAndReturn(S entity) {
        return  entityRepo.save(entity);
    }

    public Optional<S> update(Id entityId, ObjectMap overrides) {
     return update(entityId, overrides, null);
    }


    @Transactional
    public Optional<S> update(Id entityId, ObjectMap overrides, Consumer<S> customEntity) {
        Optional<S> optional = findById(entityId);
        return optional.map(s -> {
            s.assignFromMap(overrides);
            if(customEntity != null) customEntity.accept(s);
            return saveAndReturn(s);
        });
    }

    @Transactional
    public Optional<S> update(Id entityId, S overrides) {
        Optional<S> optional = findById(entityId);
        if(optional.isEmpty()) return Optional.empty();
        else {
            optional.get().assignFromEntity(overrides);
            S newUp = saveAndReturn(optional.get());
            return Optional.of(newUp);
        }
    }

    protected String getCacheName() {
        return getClass().getSimpleName()
                .replace("Service", "")
                .trim().toLowerCase()+"s";
    }

    public void clearCache() {}
}