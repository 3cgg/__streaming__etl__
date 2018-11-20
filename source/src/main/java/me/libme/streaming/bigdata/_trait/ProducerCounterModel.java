package me.libme.streaming.bigdata._trait;

import me.libme.kernel._c._m.JModel;
import me.libme.kernel._c.json.JJSONObject;

import java.util.Map;

/**
 * Created by J on 2018/11/19.
 */
public class ProducerCounterModel extends CounterModel implements JModel,JJSONObject<Map>{

    private String producerName;

    private long count;

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public Map serializableJSONObject() {
        return null;
    }
}
