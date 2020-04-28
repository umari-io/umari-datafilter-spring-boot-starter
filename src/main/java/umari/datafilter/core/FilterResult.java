package umari.datafilter.core;

import org.springframework.data.domain.Page;

/**
 * Clase que representa o resultado da filtragem de dados.
 */
public class FilterResult<T> {

    /**
     * Paginação regular.
     */
    private Page<T> page;

    public FilterResult() {
    }

    public Page<T> getPage() {
        return this.page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public String toString() {
        return "FilterResult(page=" + this.getPage() + ")";
    }
}
