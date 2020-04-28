package umari.datafilter.predicate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Classe base dos predicados de comparação e operadores lógicos (AND,OR).
 * 
 * @author jcruz
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
// @formatter:off
@JsonSubTypes({
	@Type(value=Conjunction.class, name="and"),
	@Type(value=Disjunction.class, name="or"),
	@Type(value=EqualsPredicate.class, name="="),
	@Type(value=NotEqualsToPredicate.class, name="<>"),
	@Type(value=GreaterThanPredicate.class, name=">"),
	@Type(value=GreaterThanOrEqualsToPredicate.class, name=">="),
	@Type(value=LessThanPredicate.class, name="<"),
	@Type(value=LessThanOrEqualsToPredicate.class, name="<="),
	@Type(value=ContainsPredicate.class, name="contains"),
	@Type(value=NotContainsPredicate.class, name="notcontains")
})
// @formatter:on
public abstract class Expression {
	private String type;

	public abstract String toSql();

	public abstract String toJpql();

	@Deprecated
	public abstract <T> Predicate toPredicate(Class<T> clazz, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb);

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
