package tv.vdx.assignmentvdx.dump;

/**
 * Expired keys data write strategy contract
 */
public interface DataWriteStrategy {

    void write(Object data);

    DataWriteStrategyEnum getStrategyName();
}
