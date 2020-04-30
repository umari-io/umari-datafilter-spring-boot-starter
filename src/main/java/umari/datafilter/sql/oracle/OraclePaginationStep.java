package umari.datafilter.sql.oracle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import umari.datafilter.sql.SqlPageableSelectStep;
import umari.datafilter.sql.SqlPaginationStep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OraclePaginationStep implements SqlPaginationStep {

	private JdbcTemplate jdbcTemplate;

	private Map<String, String> sqlFragments = new HashMap<>();

	public OraclePaginationStep(JdbcTemplate jdbcTemplate, Map<String, String> sqlFragments) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.sqlFragments = sqlFragments;
	}

	@Override
	public List<Map<String, Object>> fetchMaps() {
		return jdbcTemplate.queryForList(sqlFragments.get("sql"));
	}

	@Override
	public SqlPageableSelectStep limit(Pageable pageable) {
		String sql = StringUtils.isNotBlank(sqlFragments.get("whereSql")) ? sqlFragments.get("whereSql") : sqlFragments.get("sourceSql");
		sqlFragments.put("countSql", String.format("select count(0) from (%s)  ", sql));
		if (pageable.getPageNumber() == 0) {
			sqlFragments.put("sql", String.format("select * from ( %s ) where rownum <= %s", sqlFragments.get("sql"), pageable.getPageSize()));
			return new OraclePageableSelectStep(jdbcTemplate, sqlFragments, pageable);
		}
		// @formatter:off
		sqlFragments.put("sql", String.format(" select * from (  select row_.*, rownum rownum_ from ( %s ) row_ where rownum <=  %d ) where  rownum_ > %d",
				sqlFragments.get("sql"), 
				(pageable.getPageSize()*pageable.getPageNumber()+pageable.getPageSize()),
				pageable.getPageSize()*pageable.getPageNumber()));
		// @formatter:on
		return new OraclePageableSelectStep(jdbcTemplate, sqlFragments, pageable);
	}

}
