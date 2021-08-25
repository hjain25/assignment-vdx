package tv.vdx.assignmentvdx.factory;

import tv.vdx.assignmentvdx.cache.CustomCache;
import tv.vdx.assignmentvdx.cache.impl.TimedCache;
import tv.vdx.assignmentvdx.enums.CacheTypeEnum;

import java.util.concurrent.TimeUnit;

import static tv.vdx.assignmentvdx.dump.DataWriteStrategyEnum.FILE_WRITER;

/**
 * factory to create cache
 */
public class CacheFactory {

    public static <K, V> CustomCache<K, V> createCache(CacheTypeEnum cacheType){
        CustomCache<K, V> cache = null;
        switch(cacheType){
            case TIMED_CACHE:
                cache = new TimedCache<>();
                break;
            default:
                cache = new TimedCache<>(TimeUnit.SECONDS, 5, FILE_WRITER);
        }
        return cache;
    }
}
