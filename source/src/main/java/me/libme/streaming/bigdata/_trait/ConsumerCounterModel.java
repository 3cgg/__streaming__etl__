package me.libme.streaming.bigdata._trait;

import me.libme.kernel._c._m.JModel;

/**
 * Created by J on 2018/11/19.
 */
public class ConsumerCounterModel implements JModel{


    private String nodeName;

    private String consumerName;

    private long count;

    private long startTime;

    private long time;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
