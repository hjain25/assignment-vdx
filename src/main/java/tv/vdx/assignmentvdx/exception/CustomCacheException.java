package tv.vdx.assignmentvdx.exception;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class CustomCacheException extends RuntimeException{

    private String errorMessage;

    public CustomCacheException(String message){
        this.errorMessage = message;
    }
}
