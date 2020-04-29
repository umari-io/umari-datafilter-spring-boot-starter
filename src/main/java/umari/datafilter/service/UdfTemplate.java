package umari.datafilter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import umari.datafilter.core.Aggregable;
import umari.datafilter.core.Aggregation;
import umari.datafilter.core.Filterable;

import java.util.List;

/**
 * Classe de serviço com as funcionalidades de filtragem.
 */
public interface UdfTemplate {

    /**
     * Realiza a filtragem de dados para a entidade
     *
     * @param entityClass Entidade alvo da filtragem.
     * @param filterable  Lista de predicados.
     * @param pageable    Dados de paginação e ordenação.
     * @return Objeto paginado (Page) com as informações de filtragem.
     */
    <T> Page<T> filter(Class<T> entityClass, Filterable filterable, Pageable pageable);

    /**
     * Realiza a filtragem de dados para a entidade com suporte a specification.
     *
     * @param entityClass
     * @param filterable
     * @param pageable
     * @param specification
     * @param <T>
     * @return
     */
    <T> Page<T> filter(Class<T> entityClass, Filterable filterable, Pageable pageable, Specification<T> specification);

    /**
     * Realiza a operação(s) de agregação na entidade.
     *
     * @param entityClass
     * @param filterable
     * @param <T>
     * @return
     */
    <T> List<Aggregation> aggregate(Class<T> entityClass, Filterable filterable, Aggregable[] aggregables);

    /**
     * Realiza a operação(s) de agregação na entidade com suporte a specification.
     *
     * @param entityClass
     * @param filterable
     * @param aggregables
     * @param specification
     * @param <T>
     * @return
     */
    <T> List<Aggregation> aggregate(Class<T> entityClass, Filterable filterable, Aggregable[] aggregables, Specification<T> specification);

}
