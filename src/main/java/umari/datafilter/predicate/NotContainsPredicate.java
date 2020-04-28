package umari.datafilter.predicate;

import org.slf4j.Logger;

import javax.persistence.criteria.*;

/**
 * 
 * @author jcruz
 *
 */
public class NotContainsPredicate extends AbstractPredicate {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(NotContainsPredicate.class);

	@Override
	public String toSql() {
		return this.getDataField() + " not like '%" + this.getValue() + "%'";
	}

	@Override
	public String toJpql() {
		return this.getDataField() + " not like '%" + this.getValue() + "%'";
	}

	@Override
	public <T> Predicate toPredicate(Class<T> clazz, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		Class<?> dataFieldType = getDataType(clazz, this.getDataField());
		Path<?> path = root.get(this.getDataField());
		log.debug("NotContainsPredicate(field: {}, type: {}, value: {})", this.getDataField(), dataFieldType, this.getValue());

		if (dataFieldType == String.class) return cb.notLike(cb.upper(path.as(String.class)), "%" + ((String) this.getValue()).toUpperCase() + "%");
		throw new IllegalArgumentException(String.format("Não foi possível montar um predicado para o campo '%s' com o tipo 'notcontains'", this.getValue()));
	}

}
