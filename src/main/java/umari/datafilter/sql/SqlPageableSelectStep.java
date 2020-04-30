package umari.datafilter.sql;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SqlPageableSelectStep {
	Page<List<Map<String, Object>>> fetchMaps();
}
