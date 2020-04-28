package umari.datafilter.predicate;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.*;

/**
 * @author jcruz
 */
public class NotEqualsToPredicate extends AbstractPredicate {

    private static final Logger log = LoggerFactory.getLogger(NotEqualsToPredicate.class);

    @Override
    public String toSql() {
        if (isBoolean()) return this.getDataField() + " = " + ((boolean) this.getValue() ? 0 : 1);
        if (getValue().equals("@null")) return this.getDataField() + " is not null ";
        if (getValue().equals("@notnull")) return this.getDataField() + " is null ";
        return this.getDataField() + " <> " + this.surroundSingleQuotes(this.getValue());
    }

    @Override
    public String toJpql() {
        if (isBoolean()) return this.getDataField() + " is " + !(boolean) this.getValue();
        if (getValue().equals("@null")) return this.getDataField() + " is not null ";
        if (getValue().equals("@notnull")) return this.getDataField() + " is null ";
        return this.getDataField() + " <> " + this.surroundSingleQuotes(this.getValue());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> Predicate toPredicate(Class<T> clazz, Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Class<?> dataFieldType = getDataType(clazz, this.getDataField());
        Path<?> path = root.get(this.getDataField());
        log.debug("NotEqualsToPredicate(field: {}, type: {}, value: {})", this.getDataField(), dataFieldType, this.getValue());

        if (dataFieldType == Boolean.class && (boolean) this.getValue() == true)
            return cb.isFalse(path.as(Boolean.class));
        if (dataFieldType == Boolean.class && (boolean) this.getValue() == false)
            return cb.isTrue(path.as(Boolean.class));
        if (getValue() == "@notnull") return cb.isNull(path);
        if (getValue() == "@null") return cb.isNotNull(path);
        if (dataFieldType.isEnum())
            return cb.notEqual(path, Enum.valueOf((Class<Enum>) dataFieldType, String.valueOf(this.getValue())));
        return cb.notEqual(path, this.getValue());
    }

}
