package umari.datafilter.rest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umari.datafilter.core.Aggregable;
import umari.datafilter.service.EntityHelper;
import umari.datafilter.service.UdfTemplate;

import javax.annotation.PostConstruct;

@RequestMapping("/api/udf")
@RestController
@ConditionalOnWebApplication
public class UdfRest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UdfRest.class);

    @Autowired
    private UdfTemplate udfTemplate;

    @Autowired
    private EntityHelper entityHelper;

    @PostConstruct
    private void init() {
        log.info("Initialized.");
    }

    @PostMapping("/filter/{entity}")
    public ResponseEntity<Page<?>> filter(@PathVariable String entity, @RequestBody(required = false) UdfRequest udfRequest, Pageable pageable) {
        return ResponseEntity.ok(udfTemplate.filter(
                entityHelper.findClassTypeByName(entity),
                udfRequest.getFilterable(),
                pageable));
    }

    @PostMapping("/agg/{entity}")
    public ResponseEntity<?> aggreate(@PathVariable String entity, @RequestBody(required = false) UdfRequest udfRequest) {
        return ResponseEntity.ok(udfTemplate.aggregate(
                entityHelper.findClassTypeByName(entity),
                udfRequest.getFilterable(),
                udfRequest.getAggregables().toArray(new Aggregable[]{})));

    }

}
