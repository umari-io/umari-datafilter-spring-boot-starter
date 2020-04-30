package umari.datafilter.sql.h2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import umari.datafilter.sql.SqlPageableSelectStep;
import umari.datafilter.sql.SqlPaginationStep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class H2PaginationStep implements SqlPaginationStep {

	private JdbcTemplate jdbcTemplate;

	private Map<String, String> sqlFragments = new HashMap<>();

	public H2PaginationStep(JdbcTemplate jdbcTemplate, Map<String, String> sqlFragments) {
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
			sqlFragments.put("sql", String.format("%s limit %s", sqlFragments.get("sql"), pageable.getPageSize()));
			return new H2PageableSelectStep(jdbcTemplate, sqlFragments, pageable);
		}
		// @formatter:off
		sqlFragments.put("sql", String.format("%s limit %d offset %d",
				sqlFragments.get("sql"), 
				pageable.getPageSize(),
				pageable.getPageSize()*pageable.getPageNumber()));
		// @formatter:on
		return new H2PageableSelectStep(jdbcTemplate, sqlFragments, pageable);
	}

}
