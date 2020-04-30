package umari.datafilter.sql;


import umari.datafilter.core.Filterable;

public interface SqlWhereStep extends SqlFetchStep, SqlOrderByStep {
    SqlOrderByStep where(Filterable filter);
}
