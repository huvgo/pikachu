package go.pokemon.pikachu;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
class RequestQueue {

    private BlockingQueue<String> seeds = new LinkedBlockingQueue<>();

    private Set<String> allSeed = Collections.synchronizedSet(new HashSet<>());

    String poll() {
        if (seeds.isEmpty()) {
            return null;
        }
        return seeds.poll();
    }

    void push(String url) {
        boolean contains = allSeed.contains(url);
        if (!contains) {
            allSeed.add(url);
            seeds.add(url);
        }
    }
}
