package umari.datafilter.core;

import java.util.Objects;

/**
 * Classe de requisição de filtragem com a estrutura de predicados e da sumarização.
 */
public class FilterRequest {

    private Filterable filterable = new Filterable.EmptyFilterable();

    private AggregationRequest[] aggregations;

    public static boolean hasAggregable(FilterRequest payload) {
        return Objects.nonNull(payload) && Objects.nonNull(payload.getAggregations());
    }

    public Filterable getFilterable() {
        return this.filterable;
    }

    public AggregationRequest[] getAggregations() {
        return this.aggregations;
    }

    public void setFilterable(Filterable filterable) {
        this.filterable = filterable;
    }

    public void setAggregations(AggregationRequest[] aggregations) {
        this.aggregations = aggregations;
    }
}
