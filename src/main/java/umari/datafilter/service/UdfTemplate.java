package umari.datafilter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import umari.datafilter.core.Aggregable;
import umari.datafilter.core.Aggregation;
import umari.datafilter.core.Filterable;

import java.util.List;
import java.util.Map;

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
     * Realiza a filtragem de dados com paginação para a entidade com suporte a specification.
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
     * Realiza uma operação de filtragem com paginação em SQL nativo.
     *
     * @param sql
     * @param filterable
     * @param pageable
     * @return
     */
    Page<List<Map<String, Object>>> filter(String sql, Filterable filterable, Pageable pageable);

    /**
     * Realiza uma operação de filtragem sem paginação em SQL nativo.
     *
     * @param sql
     * @param sort
     * @param filter
     * @return
     */
    List<Map<String, Object>> filter(String sql, Sort sort, Filterable filter);

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

    /**
     * Realiza operação(s) de agregação em SQL nativo.
     *
     * @param sql
     * @param filterable
     * @param aggregables
     * @return
     */
    List<Aggregation> aggregate(String sql, Filterable filterable, Aggregable[] aggregables);

}
