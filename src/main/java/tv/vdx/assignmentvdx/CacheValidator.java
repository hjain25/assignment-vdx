package tv.vdx.assignmentvdx;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tv.vdx.assignmentvdx.dump.DataWriteStrategyEnum;
import tv.vdx.assignmentvdx.exception.CustomCacheException;

@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class CacheValidator {

    public static void validateInitParams(long ttl, DataWriteStrategyEnum dataWriteStrategyEnum){

        if(ttl <= 0){
            throw new CustomCacheException("ttl should be at least > 0");
        }

        if(dataWriteStrategyEnum == null){
            throw new CustomCacheException("invalid value of data writer");
        }
    }
}
