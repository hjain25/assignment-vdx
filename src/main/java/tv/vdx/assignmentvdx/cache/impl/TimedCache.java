package tv.vdx.assignmentvdx.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import tv.vdx.assignmentvdx.CacheValidator;
import tv.vdx.assignmentvdx.cache.CustomCache;
import tv.vdx.assignmentvdx.dump.DataWriteStrategy;
import tv.vdx.assignmentvdx.dump.DataWriteStrategyEnum;
import tv.vdx.assignmentvdx.dump.FileWriterStrategy;
import tv.vdx.assignmentvdx.exception.CustomCacheException;
import tv.vdx.assignmentvdx.pojos.DataNode;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.*;

import static tv.vdx.assignmentvdx.dump.DataWriteStrategyEnum.*;
import static tv.vdx.assignmentvdx.utils.TimeUtils.isExpired;

@Slf4j
public class TimedCache<K, V> implements CustomCache<K, V> {

    private long ttl;
    private Map<K, DataNode> map;
    private PriorityQueue<DataNode> timedHeap;
    private DataWriteStrategy dataWriteStrategy;

    public TimedCache(TimeUnit timeUnit, long ttl, DataWriteStrategyEnum dataWriteStrategyEnum) {

        // validation
        CacheValidator.validateInitParams(ttl, dataWriteStrategyEnum);

        map = new ConcurrentHashMap<>();
        timedHeap = new PriorityQueue<>(Comparator.comparing(t -> t.getCreationTime()));
        this.ttl = timeUnit.toSeconds(ttl);

        if(dataWriteStrategyEnum == FILE_WRITER){
            this.dataWriteStrategy = new FileWriterStrategy();
        }

        // start cleanup service
        cleanup();
    }

    public V get(K key) {

        if (map.containsKey(key)) {
            DataNode dataNode = map.get(key);
            if (isExpired(dataNode.getCreationTime(), this.ttl)) {
                dumpToFile(dataNode);
                return null;
            }
            return (V) dataNode.getValue();
        }

        return null;
    }

    public void put(K key, V value) {
        DataNode dataNode = new DataNode(key, value, LocalDateTime.now());
        map.put(key, dataNode);
        timedHeap.add(dataNode);
    }

    public double average() {
        return map.values().stream()
                .filter(e -> !isExpired(e.getCreationTime(), this.ttl))
                .mapToInt(e -> (int) e.getValue())
                .average()
                .orElse(Double.NaN);
    }


    /**
     * cleaner i.e. eagerly evicts expired keys every second.
     */
    private void cleanup() {

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
                new BasicThreadFactory.Builder().daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
            while (isExpired(this.timedHeap.peek().getCreationTime(), this.ttl)) {
                log.info("cleanup started" + LocalDateTime.now());
                DataNode dataNode = timedHeap.peek();
                map.remove(dataNode.getKey());
                dumpToFile(dataNode);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void dumpToFile(DataNode dataNode) {
        dataWriteStrategy.write(dataNode.toString());
    }
}


