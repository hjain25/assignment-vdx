package tv.vdx.assignmentvdx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheTypeEnum {

    TIMED_CACHE("TIMED_CACHE", "cache with timed eviction only"),

    LRU_CACHE("LRU_CACHE", "cache with lru eviction policy")

    ;

    private String code;

    private String description;
}
