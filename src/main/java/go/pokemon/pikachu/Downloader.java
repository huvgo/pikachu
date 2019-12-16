package go.pokemon.pikachu;

public interface Downloader {

    Response down(String url, Config config);
}
