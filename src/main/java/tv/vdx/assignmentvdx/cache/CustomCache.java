package tv.vdx.assignmentvdx.cache;

/**
 * custom cache : can be usd to ipmlement different types of cache
 * @param <K>
 * @param <V>
 */
public interface CustomCache<K, V> {

    /**
     * gets a value by key
     * returns null if key is expired
     *
     * @param key
     * @return
     */
    V get(K key);

    /**
     * puts into cache with given ttl
     *
     * @param key
     * @param value
     */
    void put(K key, V value);

    /**
     * averages out the non expired values in cache.
     * to be discussed in interview
     *
     * @return
     */
    double average();
}
