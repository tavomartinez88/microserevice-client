package com.gustavomartinez.clientkpi.model;


import org.springframework.http.HttpStatus;

public class ResponseKpiClients {

    private Double average;
    private Double standarDeviation;
    private HttpStatus status;
    private String error;

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getStandarDeviation() {
        return standarDeviation;
    }

    public void setStandarDeviation(Double standarDeviation) {
        this.standarDeviation = standarDeviation;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
