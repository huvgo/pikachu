package go.pokemon.pikachu;

import go.pokemon.pikachu.convert.HTML;
import go.pokemon.pikachu.convert.JSON;

public class Response {
    private Integer statusCode;
    private String url;
    private String body;
    private boolean done = false;

    // private HTML html;
    // private JSON json;


    public HTML getHtml() {
        return new HTML(url, body);
    }

    public JSON getJson() {
        return new JSON(body);
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    void setBody(String body) {
        this.body = body;
    }

    public boolean isDone() {
        return done;
    }

    void setDone(boolean done) {
        this.done = done;
    }
}
