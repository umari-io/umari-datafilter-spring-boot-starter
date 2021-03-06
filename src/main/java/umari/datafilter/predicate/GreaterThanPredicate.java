package umari.datafilter.predicate;

import org.slf4j.Logger;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GreaterThanPredicate extends AbstractPredicate {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(GreaterThanPredicate.class);

	@Override
	public String toSql() {
		return this.getDataField() + " > " + this.surroundSingleQuotes(this.getValue());
	}

	@Override
	public String toJpql() {
		return this.getDataField() + " > " + this.surroundSingleQuotes(this.getValue());
	}

	@Override
	public <T> Predicate toPredicate(Class<T> clazz, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		Class<?> dataFieldType = getDataType(clazz, this.getDataField());
		Path<?> path = root.get(this.getDataField());

		log.debug("GreaterThanPredicate(field: {}, type: {}, value: {})", this.getDataField(), dataFieldType, this.getValue());

		if (dataFieldType == LocalDate.class) return cb.greaterThan(path.as(LocalDate.class), getLocalDateValue(this.getValue()));
		if (dataFieldType == LocalDateTime.class) return cb.greaterThan(path.as(LocalDateTime.class), getLocalDateTimeValue(this.getValue()));
		if (dataFieldType == Long.class) return cb.gt(path.as(Long.class), ((Number) this.getValue()).longValue());
		if (dataFieldType == Integer.class) return cb.gt(path.as(Integer.class), ((Number) this.getValue()).intValue());
		if (dataFieldType == Short.class) return cb.gt(path.as(Short.class), ((Number) this.getValue()).shortValue());
		if (dataFieldType == BigDecimal.class) return cb.gt(path.as(BigDecimal.class), new BigDecimal(String.valueOf(this.getValue())));
		if (dataFieldType == BigInteger.class) return cb.gt(path.as(BigInteger.class), new BigInteger(String.valueOf(this.getValue())));
		throw new IllegalArgumentException(String.format("Não foi possível montar um predicado para o campo '%s' com o tipo '>'", this.getDataField()));
	}

	private LocalDate getLocalDateValue(Object value) {
		String stringValue = (String) this.getValue();
		if ((stringValue).equals("@now")) return LocalDate.now();
		DateTimeFormatter formatter = stringValue.contains("T") ?  DateTimeFormatter.ISO_DATE_TIME : DateTimeFormatter.ISO_DATE;
		return LocalDate.parse((String) this.getValue(), formatter);
	}

	private LocalDateTime getLocalDateTimeValue(Object value) {
		if (((String) this.getValue()).equals("@now")) return LocalDateTime.now();
		return LocalDateTime.parse((String) this.getValue(), DateTimeFormatter.ISO_DATE_TIME);
	}

}
