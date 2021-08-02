package tv.vdx.assignmentvdx.pojos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public
class DataNode<K, V> {

    private V value;
    private K key;
    private LocalDateTime creationTime;

    public DataNode(K key, V value,  LocalDateTime creationTime) {
        this.key = key;
        this.value = value;
        this.creationTime = creationTime;
    }
}
