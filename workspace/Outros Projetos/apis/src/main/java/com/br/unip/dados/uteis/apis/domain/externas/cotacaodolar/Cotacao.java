package com.br.unip.dados.uteis.apis.domain.externas.cotacaodolar;

import lombok.ToString;

@ToString
public class Cotacao {

    private String by;
    private boolean valid_key;
    Results ResultsObject;
    private float execution_time;
    private boolean from_cache;

    public String getBy() {
        return by;
    }

    public boolean getValid_key() {
        return valid_key;
    }

    public Results getResults() {
        return ResultsObject;
    }

    public float getExecution_time() {
        return execution_time;
    }

    public boolean getFrom_cache() {
        return from_cache;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setValid_key(boolean valid_key) {
        this.valid_key = valid_key;
    }

    public void setResults(Results resultsObject) {
        this.ResultsObject = resultsObject;
    }

    public void setExecution_time(float execution_time) {
        this.execution_time = execution_time;
    }

    public void setFrom_cache(boolean from_cache) {
        this.from_cache = from_cache;
    }

}
