package umari.datafilter.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import umari.datafilter.UdfConfig;
import umari.datafilter.service.UdfTemplate;

import javax.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = UdfConfig.class)
class UdfJpaTemplateTest {

    @Autowired
    private UdfTemplate udfTemplate;

    @Test
    void filter() {

    }

    @Test
    void aggregate() {
    }
}