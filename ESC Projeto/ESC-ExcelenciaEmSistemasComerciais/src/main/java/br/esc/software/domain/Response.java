package br.esc.software.domain;

public class Response {

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Response [response=" + response + "]";
    }

}
