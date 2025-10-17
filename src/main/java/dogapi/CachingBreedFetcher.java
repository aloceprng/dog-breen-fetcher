package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher tempFetcher;
    private int callsMade = 0;
    private final Map<String, List<String>> cached = new HashMap<>();
    public CachingBreedFetcher(BreedFetcher fetcher) {
        tempFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (cached.containsKey(breed)) {
            return cached.get(breed);
        }
        callsMade++;
        try {
            cached.put(breed, tempFetcher.getSubBreeds(breed));

        } catch (BreedNotFoundException e) {
            throw new BreedNotFoundException(breed);
        }
        return cached.get(breed);

    }

    public int getCallsMade() {
        return callsMade;
    }
}