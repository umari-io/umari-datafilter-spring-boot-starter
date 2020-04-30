package umari.datafilter.sql.postgre;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import umari.datafilter.core.Aggregable;
import umari.datafilter.core.Aggregation;
import umari.datafilter.core.Filterable;
import umari.datafilter.sql.SqlContext;
import umari.datafilter.sql.SqlWhereStep;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostgreContextImpl implements SqlContext {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PostgreContextImpl.class);

    private JdbcTemplate jdbcTemplate;

    private Map<String, String> sqlFragments = new HashMap<>();

    public PostgreContextImpl(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SqlWhereStep selectFrom(String sql) {
        String selectSql = StringUtils.prependIfMissing(sql, "select * from (");
        selectSql = StringUtils.appendIfMissing(selectSql, " ) ");

        sqlFragments.put("sourceSql", sql);
        sqlFragments.put("sql", selectSql);
        return new PostgreWhereStep(jdbcTemplate, sqlFragments);
    }

    @Override
    public List<Aggregation> aggregate(String sql, Filterable filterable, Aggregable[] aggregables) {
        String whereSql = filterable instanceof Filterable.EmptyFilterable ? "" : String.format("where %s", filterable.toSql());
        String aggFragmentsSql = Arrays.asList(aggregables).stream()
                .map(agg -> String.format(agg.getOperation().getSqlFragment(), agg.getDataField(), agg.getDataField()))
                .collect(Collectors.joining(","));
        String aggSql = String.format("select %s from ( %s ) %s ", aggFragmentsSql, sql, whereSql);
        log.debug("");
        log.debug(aggSql);
        log.debug("");
        Map<String, Object> result = jdbcTemplate.queryForMap(aggSql);
        return Arrays.asList(aggregables).stream()
                .map(agg -> new Aggregation(agg.getDataField(), result.get(agg.getDataField()), agg.getOperation()))
                .collect(Collectors.toList());
    }


    @Override
    public void clear() {
        sqlFragments.clear();
    }

}
