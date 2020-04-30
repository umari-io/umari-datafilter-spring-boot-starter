package umari.datafilter.sql.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import umari.datafilter.sql.SqlPageableSelectStep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLPageableSelectStep implements SqlPageableSelectStep {

	private JdbcTemplate jdbcTemplate;

	private Map<String, String> sqlFragments = new HashMap<>();

	private Pageable pageable;

	public MySQLPageableSelectStep(JdbcTemplate jdbcTemplate, Map<String, String> sqlFragments, Pageable pageable) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.sqlFragments = sqlFragments;
		this.pageable = pageable;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<List<Map<String, Object>>> fetchMaps() {
		Long count = jdbcTemplate.queryForObject(sqlFragments.get("countSql"), Long.class);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sqlFragments.get("sql"));
		return new PageImpl(result, pageable, count);
	}

}
