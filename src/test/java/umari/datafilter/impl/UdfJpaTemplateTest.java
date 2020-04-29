package umari.datafilter.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import umari.datafilter.UdfConfig;
import umari.datafilter.core.Filterable;
import umari.datafilter.domain.Foo;
import umari.datafilter.predicate.Conjunction;
import umari.datafilter.predicate.EqualsPredicate;
import umari.datafilter.predicate.LessThanPredicate;
import umari.datafilter.service.UdfTemplate;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = UdfConfig.class)
class UdfJpaTemplateTest {

    @Autowired
    private UdfTemplate udfTemplate;

    @Test
    @Sql("classpath:foo.sql")
    void filter_equalsPredicate_nome() {
        Conjunction conjunction = new Conjunction();
        EqualsPredicate equalsPredicate = new EqualsPredicate();
        equalsPredicate.setDataField("nome");
        equalsPredicate.setValue("José Ribamar Monteiro");
        conjunction.getPredicates().add(equalsPredicate);
        Filterable filterable = conjunction;

        Page<Foo> foos = udfTemplate.filter(Foo.class, filterable, PageRequest.of(0, 5));
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
    void aggregate() {
    }
}