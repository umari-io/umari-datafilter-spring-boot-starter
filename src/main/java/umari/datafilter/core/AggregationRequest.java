package umari.datafilter.core;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe com informações para a realização de uma operação de agregação.
 */
public class AggregationRequest {

    /**
     * Nome do atributo que sofrerá a operação de agregação.
     */
    @JsonAlias("selector")
    private String dataField;

    /**
     * Tipo de sumarização a ser realizada.
     *
     * @see Operation
     */
    @JsonAlias("summaryType")
    private Operation operation;

    public String getDataField() {
        return this.dataField;
    }

    public Operation getOperation() {
        return this.operation;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public enum Operation {
        @JsonProperty("sum") SUM,
        @JsonProperty("max") MAX,
        @JsonProperty("min") MIN,
        @JsonProperty("count") COUNT,
        @JsonProperty("avg") AVG
    }
}
