package tv.vdx.assignmentvdx;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tv.vdx.assignmentvdx.cache.CustomCache;
import tv.vdx.assignmentvdx.cache.impl.TimedCache;
import tv.vdx.assignmentvdx.dump.DataWriteStrategyEnum;
import tv.vdx.assignmentvdx.dump.FileWriterStrategy;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.vdx.assignmentvdx.dump.DataWriteStrategyEnum.FILE_WRITER;

@ExtendWith(MockitoExtension.class)
public class TimedCacheTest {

    // below are basic tests to check cache working with expired and non expired keys
    // If given time, gtests can be extended to test on single instance of cache with multi-threads.

    @Test
    public void givenDataThenShouldWriteExpiredKeysToFile() throws InterruptedException {

        CustomCache<String, Integer> timedCache = new TimedCache(TimeUnit.SECONDS, 5, FILE_WRITER);
        timedCache.put("1", 1);
        Thread.sleep(200);
        timedCache.put("2", 2);
        Thread.sleep(200);
        timedCache.put("3", 3);
        Thread.sleep(6000);

        assertThat(timedCache.get("1")).isNull();
    }

    @Test
    public void givenDataThenShouldReturnNonExpiredData() throws InterruptedException {

        CustomCache<String, Integer> timedCache = new TimedCache(TimeUnit.SECONDS, 5, FILE_WRITER);
        timedCache.put("1", 1);
        Thread.sleep(200);
        timedCache.put("2", 2);
        Thread.sleep(200);
        timedCache.put("3", 3);
        Thread.sleep(4000);

        assertThat(timedCache.get("1")).isEqualTo(1);
    }
}
