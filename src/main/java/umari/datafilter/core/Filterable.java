package umari.datafilter.core;

import umari.datafilter.predicate.Expression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Filterable extends Expression {

    private List<Expression> predicates = new ArrayList<>();

    @Override
    public String toSql() {
        // @formatter:off
        return this.predicates
                .stream()
                .map(p -> p.toSql())
                .collect(Collectors.joining());
        // @formatter:on
    }

    @Override
    public String toJpql() {
        // @formatter:off
        return this.predicates
                .stream()
                .map(p -> p.toJpql())
                .collect(Collectors.joining());
        // @formatter:on
    }

    public List<Expression> getPredicates() {
        return this.predicates;
    }

    public void setPredicates(List<Expression> predicates) {
        this.predicates = predicates;
    }

    public static class EmptyFilterable extends umari.datafilter.core.Filterable {

        @Override
        public <T> Predicate toPredicate(Class<T> clazz, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
            return null;
        }

    }
}
