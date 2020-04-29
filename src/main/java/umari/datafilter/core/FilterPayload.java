package umari.datafilter.core;

import java.util.Objects;

/**
 * Classe de requisição de filtragem com a estrutura de predicados e da sumarização.
 */
public class FilterPayload {

    private Filterable filterable = new Filterable.EmptyFilterable();

    private Aggregable[] aggregations;

    public static boolean hasAggregable(FilterPayload filterPayload) {
        return Objects.nonNull(filterPayload) && Objects.nonNull(filterPayload.getAggregations());
    }

    public Filterable getFilterable() {
        return this.filterable;
    }

    public Aggregable[] getAggregations() {
        return this.aggregations;
    }

    public void setFilterable(Filterable filterable) {
        this.filterable = filterable;
    }

    public void setAggregations(Aggregable[] aggregations) {
        this.aggregations = aggregations;
    }
}
