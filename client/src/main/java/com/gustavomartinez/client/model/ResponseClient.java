package com.gustavomartinez.client.model;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component("responseClient")
public class ResponseClient {

    private HttpStatus status;
    private String error;

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

    @Override
    public String toString() {
        return "ResponseClient{" +
                "status=" + status +
                ", error='" + error + '\'' +
                '}';
    }
}
