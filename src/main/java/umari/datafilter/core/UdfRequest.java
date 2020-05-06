package umari.datafilter.core;

import umari.datafilter.core.Aggregable;
import umari.datafilter.core.Filterable;

import java.util.List;

/**
 * Classe que representa uma requisição de filtragem e agregação.
 */
public class UdfRequest {
    private Filterable filterable;

    private List<Aggregable> aggregables;

    public Filterable getFilterable() {
        return filterable;
    }

    public void setFilterable(Filterable filterable) {
        this.filterable = filterable;
    }

    public List<Aggregable> getAggregables() {
        return aggregables;
    }

    public void setAggregables(List<Aggregable> aggregables) {
        this.aggregables = aggregables;
    }
}
