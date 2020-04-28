package umari.datafilter.predicate;


import umari.datafilter.core.Filterable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Collectors;

/**
 * Classe que representa uma operação AND.
 * 
 * @author jcruz
 *
 */
public class Conjunction extends Filterable {

	@Override
	public String toSql() {
		String[] delimiter = this.getPredicates().size() > 1 ? new String[] { "(", ")" } : new String[] { " ", " " };
		// @formatter:off
		return this.getPredicates()
				.stream().map(p -> p.toSql())
				.collect((Collectors.joining(" and ", delimiter[0], delimiter[1])));
		// @formatter:on
	}

	@Override
	public String toJpql() {
		return super.toJpql();
	}

	@Override
	public <T> Predicate toPredicate(Class<T> clazz, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		// @formatter:off
		return cb.and(this.getPredicates()
				.stream()
				.map(p -> p.toPredicate(clazz, root, cq, cb))
				.toArray(Predicate[]::new));
		// @formatter:on
	}

}
