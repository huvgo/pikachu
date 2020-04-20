package go.pokemon.pikachu;


import go.pokemon.pikachu.util.Assert;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientDownloader implements Downloader {

    private HttpClient httpClient = HttpClient.newBuilder().cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL)).followRedirects(HttpClient.Redirect.NORMAL).build();

    @Override
    public Response down(String url, Config config) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofMillis(config.timeOut)).build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.notNull(response);
        return generatorPage(response);
    }

    private Response generatorPage(HttpResponse<String> httpResponse) {
        Response response = new Response();
        response.setStatusCode(httpResponse.statusCode());
        response.setBody(httpResponse.body());
        response.setUrl(httpResponse.uri().toString());
        response.setDone(true);
        return response;
    }
}
