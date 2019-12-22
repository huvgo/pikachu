package go.pokemon.pikachu;

import lombok.ToString;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
public class PikachuContext {

    private Response response;

    private Set<String> request;

    PikachuContext() {
    }

    public Response response() {
        return response;
    }

    void setResponse(Response response) {
        this.response = response;
    }

    Set<String> getRequest() {
        return request;
    }

    public void addRequest(String... urls) {
        if (this.request == null) {
            this.request = new HashSet<>();
        }
        if (urls != null) {
            this.request.addAll(Arrays.asList(urls));
        }
    }


    public void addRequest(List<String> urls) {
        if (this.request == null) {
            this.request = new HashSet<>();
        }
        if (urls != null) {
            this.request.addAll(urls);
        }
    }

}
