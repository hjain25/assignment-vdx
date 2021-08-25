package tv.vdx.assignmentvdx.dump;

/**
 * Expired keys data write strategy contract
 */
public interface DataWriteStrategy {

    /**
     * write data to storage
     * @param data
     */
    void write(Object data);

    DataWriteStrategyEnum getStrategyName();
}
