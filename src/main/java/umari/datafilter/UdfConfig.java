package umari.datafilter;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import umari.datafilter.impl.UdfJpaTemplateImpl;
import umari.datafilter.service.UdfTemplate;

@Configuration
@AutoConfigurationPackage
public class UdfConfig {

    @Bean
    public UdfTemplate getUdfTemplate() {
        return new UdfJpaTemplateImpl();
    }
}
