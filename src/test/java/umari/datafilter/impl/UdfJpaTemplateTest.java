package umari.datafilter.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import umari.datafilter.UdfConfig;
import umari.datafilter.core.Aggregable;
import umari.datafilter.core.Aggregation;
import umari.datafilter.core.Filterable;
import umari.datafilter.domain.Foo;
import umari.datafilter.predicate.Conjunction;
import umari.datafilter.predicate.EqualsPredicate;
import umari.datafilter.predicate.LessThanPredicate;
import umari.datafilter.service.UdfTemplate;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static umari.datafilter.specification.FooSpecification.isMasculino;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = UdfConfig.class)
@TestPropertySource(properties = {"logging.level.umari=debug"})
class UdfJpaTemplateTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UdfJpaTemplateTest.class);

    @Autowired
    private UdfTemplate udfTemplate;

    @Test
    @Sql("classpath:foo.sql")
    void filter_equalsPredicate_nome() throws JsonProcessingException {
        Conjunction conjunction = new Conjunction();
        EqualsPredicate equalsPredicate = new EqualsPredicate();
        equalsPredicate.setDataField("nome");
        equalsPredicate.setValue("José Ribamar Monteiro");
        conjunction.getPredicates().add(equalsPredicate);
        Filterable filterable = conjunction;

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writer(new DefaultPrettyPrinter()).writeValueAsString(filterable));

        Page<Foo> foos = udfTemplate.filter(Foo.class, filterable, PageRequest.of(0, 5));
        Assertions.assertEquals(1, foos.getTotalElements());
    }

    @Test
    @Sql("classpath:foo.sql")
    void filter_H2_equalsPredicate_nome() {
        Conjunction conjunction = new Conjunction();
        EqualsPredicate equalsPredicate = new EqualsPredicate();
        equalsPredicate.setDataField("nome");
        equalsPredicate.setValue("José Ribamar Monteiro");
        conjunction.getPredicates().add(equalsPredicate);
        Filterable filterable = conjunction;

        String sql = "SELECT * FROM FOO";


        Page<List<Map<String, Object>>> foos = udfTemplate.filter(sql, filterable, PageRequest.of(0, 5));
        Assertions.assertEquals(1, foos.getTotalElements());
    }

    @Test
    @Sql("classpath:foo.sql")
    void filter_lessThanPredicate_idade() {
        Conjunction conjunction = new Conjunction();
        LessThanPredicate lessThanPredicate = new LessThanPredicate();
        lessThanPredicate.setDataField("idade");
        lessThanPredicate.setValue(35);
        conjunction.getPredicates().add(lessThanPredicate);
        Filterable filterable = conjunction;

        Page<Foo> foos = udfTemplate.filter(Foo.class, filterable, PageRequest.of(0, 5));
        Assertions.assertEquals(4, foos.getTotalElements());
    }

    @Test
    @Sql("classpath:foo.sql")
    void filter_lessThanPredicate_idade_withSpecification() {
        Conjunction conjunction = new Conjunction();
        LessThanPredicate lessThanPredicate = new LessThanPredicate();
        lessThanPredicate.setDataField("idade");
        lessThanPredicate.setValue(35);
        conjunction.getPredicates().add(lessThanPredicate);
        Filterable filterable = conjunction;

        Page<Foo> foos = udfTemplate.filter(Foo.class, filterable, PageRequest.of(0, 5), isMasculino());
        Assertions.assertEquals(2, foos.getTotalElements());
    }

    @Test
    @Sql("classpath:foo.sql")
    void aggregate_count_nome() {
        Conjunction conjunction = new Conjunction();
        EqualsPredicate equalsPredicate = new EqualsPredicate();
        equalsPredicate.setDataField("genero");
        equalsPredicate.setValue(Foo.Genero.MASCULINO);
        conjunction.getPredicates().add(equalsPredicate);
        Filterable filterable = conjunction;

        Aggregable aggregable = new Aggregable();
        aggregable.setDataField("nome");
        aggregable.setOperation(Aggregable.Operation.COUNT);

        Aggregable aggregable2 = new Aggregable();
        aggregable2.setDataField("idade");
        aggregable2.setOperation(Aggregable.Operation.SUM);

        List<Aggregation> aggregations = udfTemplate.aggregate(Foo.class, filterable, new Aggregable[]{aggregable, aggregable2});

        log.info("");
        log.info("Aggregations: {}", aggregations);
        log.info("");

        Assertions.assertEquals(4L, aggregations.get(0).getResult());
        Assertions.assertEquals(139L, aggregations.get(1).getResult());
    }

    @Test
    @Sql("classpath:foo.sql")
    void aggregate_count_nome_sql() {
        Conjunction conjunction = new Conjunction();
        EqualsPredicate equalsPredicate = new EqualsPredicate();
        equalsPredicate.setDataField("genero");
        equalsPredicate.setValue(Foo.Genero.MASCULINO);
        conjunction.getPredicates().add(equalsPredicate);
        Filterable filterable = conjunction;

        Aggregable aggregable = new Aggregable();
        aggregable.setDataField("nome");
        aggregable.setOperation(Aggregable.Operation.COUNT);

        Aggregable aggregable2 = new Aggregable();
        aggregable2.setDataField("idade");
        aggregable2.setOperation(Aggregable.Operation.SUM);

        String sql = "SELECT * FROM FOO";

        List<Aggregation> aggregations = udfTemplate.aggregate(sql, filterable, new Aggregable[]{aggregable, aggregable2});

        log.info("");
        log.info("Aggregations: {}", aggregations);
        log.info("");

        Assertions.assertEquals(4L, aggregations.get(0).getResult());
        Assertions.assertEquals(139L, aggregations.get(1).getResult());
    }
}