package tv.vdx.assignmentvdx.cache;

import lombok.extern.slf4j.Slf4j;
import tv.vdx.assignmentvdx.cache.impl.TimedCache;

import java.util.concurrent.TimeUnit;

@Slf4j
public class EvictionTask<K, V> implements Runnable {

    private final TimedCache<K,V> cache;

    public EvictionTask(TimedCache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                TimeUnit.SECONDS.sleep(100);
                cache.clean();
            } catch (InterruptedException e) {
                log.error(e.getMessage() + " " + e.getStackTrace());
            }
        }

    }
}
