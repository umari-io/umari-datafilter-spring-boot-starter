package umari.datafilter.sql;

import umari.datafilter.core.Aggregable;
import umari.datafilter.core.Aggregation;
import umari.datafilter.core.Filterable;

import java.util.List;

public interface SqlContext {
    SqlWhereStep selectFrom(String sql);

    List<Aggregation> aggregate(String sql, Filterable filterable, Aggregable[] aggregables);

    void clear();
}
