package tv.vdx.assignmentvdx;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.util.EnumUtils;
import tv.vdx.assignmentvdx.dump.DataWriteStrategyEnum;
import tv.vdx.assignmentvdx.exception.CustomCacheException;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

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
