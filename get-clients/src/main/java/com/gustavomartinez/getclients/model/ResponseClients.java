package com.gustavomartinez.getclients.model;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ResponseClients {

    private List<Client> clients;
    private HttpStatus status;

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ResponseClients{" +
                "clients=" + clients +
                ", status=" + status +
                '}';
    }
}
