package umari.datafilter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Realiza a operação(s) de agregação na entidade.
     *
     * @param entityClass
     * @param filterable
     * @param <T>
     * @return
     */
    <T> List<Aggregation> aggregate(Class<T> entityClass, Filterable filterable, Aggregable... aggregables);

}
