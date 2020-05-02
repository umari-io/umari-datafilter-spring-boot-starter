package umari.datafilter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Component
public class EntityHelper {

    @Autowired
    private EntityManagerFactory emf;

    public Class<?> findClassTypeByName(String entityName) {
        return emf.getMetamodel().getEntities().stream()
                .filter(entityType -> entityType.getName().equals(entityName))
                .map(entityType -> entityType.getJavaType())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Entitade %s não encontrada no projeto.", entityName)));
    }
}
