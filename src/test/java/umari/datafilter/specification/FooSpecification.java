package umari.datafilter.specification;

import org.springframework.data.jpa.domain.Specification;
import umari.datafilter.domain.Foo;

public class FooSpecification {

    public static Specification<Foo> isMasculino() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("genero"), Foo.Genero.MASCULINO);
    }
}
