package tv.vdx.assignmentvdx.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtils {

    public static boolean isExpired(LocalDateTime lastWriteTime, long ttl){
        return ChronoUnit.SECONDS.between(lastWriteTime, LocalDateTime.now()) > ttl;
    }
}
