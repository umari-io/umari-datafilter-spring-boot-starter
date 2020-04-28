package umari.datafilter.predicate;

import org.slf4j.Logger;

import javax.persistence.criteria.*;

/**
 * Classe que representa uma condição de igualdade (=).
 *
 * @author jcruz
 *
 */
public class EqualsPredicate extends AbstractPredicate {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(EqualsPredicate.class);

	@Override
	public String toSql() {
		if (isBoolean()) return this.getDataField() + " = " + ((boolean) this.getValue() ? 1 : 0);
		if (getValue().equals("@null")) return String.format("%s is null ", this.getDataField());
		if (getValue().equals("@notnull")) return String.format("%s is not null ", this.getDataField());
		return this.getDataField() + " = " + this.surroundSingleQuotes(this.getValue());
	}

	@Override
	public String toJpql() {
		if (isBoolean()) return this.getDataField() + " is " + (boolean) this.getValue();
		if (getValue().equals("@null")) return "${this.dataField} is null ";
		if (getValue().equals("@notnull")) return "${this.dataField} is not null ";
		return this.getDataField() + " = " + this.surroundSingleQuotes(this.getValue());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Predicate toPredicate(Class<T> clazz, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		Class<?> dataFieldType =  getDataType(clazz, this.getDataField());
		Path<?> path =  root.get(this.getDataField());

		log.debug("EqualsPredicate(field: {}, type: {}, value: {})", this.getDataField(), dataFieldType, this.getValue());

		if (dataFieldType == Boolean.class && (boolean) this.getValue() == true) return cb.isTrue(path.as(Boolean.class));
		if (dataFieldType == Boolean.class && (boolean) this.getValue() == false) return cb.isFalse(path.as(Boolean.class));
		if (this.getValue() == "@notnull") return cb.isNotNull(path);
		if (this.getValue() == "@null") return cb.isNull(path);
		if (dataFieldType.isEnum()) return cb.equal(path, Enum.valueOf((Class<Enum>) dataFieldType, String.valueOf(this.getValue())));
		return cb.equal(path, this.getValue());
	}
}
