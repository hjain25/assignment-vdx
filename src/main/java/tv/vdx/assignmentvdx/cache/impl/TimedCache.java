package tv.vdx.assignmentvdx.cache.impl;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import tv.vdx.assignmentvdx.CacheValidator;
import tv.vdx.assignmentvdx.cache.CustomCache;
import tv.vdx.assignmentvdx.cache.EvictionTask;
import tv.vdx.assignmentvdx.dump.DataWriteStrategy;
import tv.vdx.assignmentvdx.dump.DataWriteStrategyEnum;
import tv.vdx.assignmentvdx.dump.FileWriterStrategy;
import tv.vdx.assignmentvdx.pojos.DataNode;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static tv.vdx.assignmentvdx.dump.DataWriteStrategyEnum.FILE_WRITER;
import static tv.vdx.assignmentvdx.utils.TimeUtils.isExpired;

@Slf4j
@NoArgsConstructor
@Setter
public class TimedCache<K, V> implements CustomCache<K, V> {

    private long ttl;
    private Map<K, DataNode> map;
    private PriorityQueue<DataNode> timedHeap;
    private DataWriteStrategy dataWriteStrategy;
    private TimeUnit timeUnit;

    public TimedCache(TimeUnit timeUnit, long ttl, DataWriteStrategyEnum dataWriteStrategyEnum) {

        // validation
        CacheValidator.validateInitParams(ttl, dataWriteStrategyEnum);

        map = new ConcurrentHashMap<>();
        timedHeap = new PriorityQueue<>(Comparator.comparing(t -> t.getCreationTime()));
        this.ttl = timeUnit.toSeconds(ttl);

        if (dataWriteStrategyEnum == FILE_WRITER) {
            this.dataWriteStrategy = new FileWriterStrategy();
        }

        // start cleanup task
        clean();
    }

    private void init(){
        Thread evictionThread = new Thread(new EvictionTask<>(this));
        evictionThread.setDaemon(true);
        evictionThread.start();
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

        // can be made async
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
    public void clean() {

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
                new BasicThreadFactory.Builder().daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
            log.info(" cleanup scheduler started ");
            while (isExpired(this.timedHeap.peek().getCreationTime(), this.ttl)) {
                DataNode dataNode = timedHeap.poll();
                map.remove(dataNode.getKey());
                dumpToFile(dataNode);
                log.info(" cleaning, cache size : " +  map.size());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void dumpToFile(DataNode dataNode) {
        dataWriteStrategy.write(dataNode.toString());
    }
}


