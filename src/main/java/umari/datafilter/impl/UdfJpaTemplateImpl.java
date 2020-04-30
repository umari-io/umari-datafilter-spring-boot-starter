package umari.datafilter.impl;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import umari.datafilter.core.Aggregable;
import umari.datafilter.core.Aggregation;
import umari.datafilter.core.Filterable;
import umari.datafilter.service.UdfTemplate;
import umari.datafilter.sql.SqlContext;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.Function;

/**
 * Implementação do template de filtragem para JPA.
 */
public class UdfJpaTemplateImpl implements UdfTemplate {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SqlContext sqlContext;

    /**
     * Mapa com os definições de código das operações de agregação.
     */
    private Map<Aggregable.Operation, Function<Triple<String, Root<?>, CriteriaBuilder>, Expression<? extends Number>>> operationsDef = new HashMap<>();

    @PostConstruct
    private void init() {
        operationsDef.put(Aggregable.Operation.COUNT, t -> t.getRight().count(t.getMiddle().get(t.getLeft())));
        operationsDef.put(Aggregable.Operation.DCOUNT, t -> t.getRight().countDistinct(t.getMiddle().get(t.getLeft())));
        operationsDef.put(Aggregable.Operation.SUM, t -> t.getRight().sum(t.getMiddle().get(t.getLeft())));
        operationsDef.put(Aggregable.Operation.MIN, t -> t.getRight().min(t.getMiddle().get(t.getLeft())));
        operationsDef.put(Aggregable.Operation.MAX, t -> t.getRight().max(t.getMiddle().get(t.getLeft())));
        operationsDef.put(Aggregable.Operation.AVG, t -> t.getRight().avg(t.getMiddle().get(t.getLeft())));
    }

    @Override
    public <T> Page<T> filter(Class<T> entityClass, Filterable filterable, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        prepareWhere(entityClass, root, cq, cb, filterable);
        TypedQuery<T> q = this.createQuery(cq, pageable);
        Long count = this.count(entityClass, filterable);
        return new PageImpl<>(q.getResultList(), pageable, count);
    }

    @Override
    public <T> Page<T> filter(Class<T> entityClass, Filterable filterable, Pageable pageable, Specification<T> specification) {
        if (Objects.isNull(specification)) return filter(entityClass, filterable, pageable, specification);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        prepareWhere(entityClass, root, cq, cb, filterable, specification);
        TypedQuery<T> q = this.createQuery(cq, pageable);
        Long count = this.count(entityClass, filterable, specification);
        return new PageImpl<>(q.getResultList(), pageable, count);
    }

    @Override
    public Page<List<Map<String, Object>>> filter(String sql, Filterable filterable, Pageable pageable) {
        Page<List<Map<String, Object>>> page = sqlContext.selectFrom(sql)
                .where(filterable)
                .orderBy(pageable.getSort())
                .limit(pageable)
                .fetchMaps();
        sqlContext.clear();
        return page;
    }

    @Override
    public List<Map<String, Object>> filter(String sql, Sort sort, Filterable filter) {
        List<Map<String, Object>> list;
        if (Objects.nonNull(filter)) {
            list = sqlContext.selectFrom(sql)
                    .where(filter)
                    .orderBy(sort)
                    .fetchMaps();
            sqlContext.clear();
            return list;
        }
        list = sqlContext.selectFrom(sql)
                .orderBy(sort)
                .fetchMaps();
        sqlContext.clear();
        return list;
    }

    @Override
    public <T> List<Aggregation> aggregate(Class<T> entityClass, Filterable filterable, Aggregable[] aggregables) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<T> root = cq.from(entityClass);
        prepareSelect(entityClass, root, cq, cb, aggregables);
        prepareWhere(entityClass, root, cq, cb, filterable);
        return getAggregations(aggregables, cq);
    }

    @Override
    public <T> List<Aggregation> aggregate(Class<T> entityClass, Filterable filterable, Aggregable[] aggregables, Specification<T> specification) {
        if (Objects.isNull(specification)) return aggregate(entityClass, filterable, aggregables);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<T> root = cq.from(entityClass);
        prepareSelect(entityClass, root, cq, cb, aggregables);
        prepareWhere(entityClass, root, cq, cb, filterable, specification);
        return getAggregations(aggregables, cq);
    }

    @Override
    public List<Aggregation> aggregate(String sql, Filterable filterable, Aggregable[] aggregables) {
        List<Aggregation> aggregations = sqlContext.aggregate(sql, filterable, aggregables);
        sqlContext.clear();
        return aggregations;
    }

    private List<Aggregation> getAggregations(Aggregable[] aggregables, CriteriaQuery<Tuple> cq) {
        TypedQuery<Tuple> query = em.createQuery(cq);
        Tuple tuple = query.getSingleResult();
        List<Aggregation> aggregations = new ArrayList<>();
        for (int i = 0; i < tuple.getElements().size(); i++) {
            aggregations.add(new Aggregation(aggregables[i].getDataField(), tuple.get(i), aggregables[i].getOperation()));
        }
        return aggregations;
    }

    private <T> Long count(Class<T> entityClass, Filterable filterable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        prepareWhere(entityClass, root, cq, cb, filterable);
        TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    private <T> Long count(Class<T> entityClass, Filterable filterable, Specification<T> specification) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        prepareWhere(entityClass, root, cq, cb, filterable, specification);
        TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    /**
     * Prepara a clausula select para o método aggregate. Caso haja mais de um operação de agregação estas serão realizadas em apenas uma consulta ao banco.
     *
     * @param entityClass
     * @param root
     * @param cq
     * @param cb
     * @param <T>
     */
    private <T> void prepareSelect(Class<T> entityClass, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb, Aggregable... aggregables) {
        Selection[] selections = Arrays.asList(aggregables)
                .stream()
                .map(agg -> operationsDef.get(agg.getOperation()).apply(Triple.of(agg.getDataField(), root, cb)))
                .toArray(Selection[]::new);
        cq.multiselect(selections);
    }

    /**
     * Prepara a clausula where com as informações dos predicados do filtro.
     *
     * @param entityClass
     * @param root
     * @param cq
     * @param cb
     * @param filterable
     * @param <T>
     * @param <X>
     */
    private <T, X> void prepareWhere(Class<T> entityClass, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb, Filterable filterable) {
        cq.where(filterable.toPredicate(entityClass, root, cq, cb));
    }

    /**
     * Prepara a clausula where com informações dos predicados do filtro e da specification.
     *
     * @param entityClass
     * @param root
     * @param cq
     * @param cb
     * @param filterable
     * @param spec
     * @param <T>
     * @param <X>
     */
    private <T, X> void prepareWhere(Class<T> entityClass, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb, Filterable filterable, Specification<T> spec) {
        if (Objects.nonNull(spec) && Objects.nonNull(spec.toPredicate(root, cq, cb))) {
            cq.where(filterable.toPredicate(entityClass, root, cq, cb), spec.toPredicate(root, cq, cb));
            return;
        }
        this.prepareWhere(entityClass, root, cq, cb, filterable);
    }

    /**
     * Prepara a query com paginação.
     *
     * @param cq
     * @param pageable
     * @param <X>
     * @return
     */
    private <X> TypedQuery<X> createQuery(CriteriaQuery<X> cq, Pageable pageable) {
        TypedQuery<X> q = em.createQuery(cq);
        if (Objects.nonNull(pageable)) {
            q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            q.setMaxResults(pageable.getPageSize());
        }
        return q;
    }

}
