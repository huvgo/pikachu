package go.pokemon.pikachu;

import go.pokemon.pikachu.convert.HTML;
import go.pokemon.pikachu.convert.JSON;

public class Response {
    private Integer statusCode;
    private String url;
    private String body;
    private boolean done = false;

    private HTML html;
    private JSON json;

    public HTML getHtml() {
        if (html == null) {
            html = new HTML(url, body);
        }
        return html;
    }

    public boolean isJson() {
        Object parse = com.alibaba.fastjson.JSON.parse(body);
        return done;
    }

    public JSON getJson() {
        if (json == null) {
            if (JSON.isJSON(body)) {
                json = new JSON(body);
            } else {
                throw new IllegalArgumentException("This is not JSON data");
            }
        }
        return json;
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
