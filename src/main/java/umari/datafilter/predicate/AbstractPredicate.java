package umari.datafilter.predicate;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public abstract class AbstractPredicate extends Expression {
	private String dataField;
	private Object value;

	protected Object surroundSingleQuotes(Object value) {
		if (value instanceof String || value instanceof Date || value instanceof LocalDate || value instanceof LocalDateTime) {
			return "'" + value + "'";
		}
		return value;
	}

	protected <T> Class<?> getDataType(Class<T> clazz, String dataField) {
		Field field = FieldUtils.getField(clazz, dataField, true);
		if (Objects.isNull(field)) {
			throw new IllegalArgumentException(String.format("Não foi possível encontrar o atributo '%s' na classe '%s'. Defina o mapeamento em '%sSpecification'", dataField, clazz));
		}
		return field.getType();
	}

	protected boolean isDate() {
		return value instanceof Date || value instanceof LocalDate || value instanceof LocalDateTime || value instanceof java.sql.Date || value instanceof Timestamp;
	}

	protected boolean isNumber() {
		return value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof BigDecimal || value instanceof Double;
	}

	protected boolean isString() {
		return value instanceof String;
	}

	protected boolean isBoolean() {
		return value instanceof Boolean;
	}

	public String getDataField() {
		return this.dataField;
	}

	public Object getValue() {
		return this.value;
	}

	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
