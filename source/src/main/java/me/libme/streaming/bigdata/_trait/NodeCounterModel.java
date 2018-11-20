package me.libme.streaming.bigdata._trait;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on 2018/11/20.
 */
public class NodeCounterModel {

    private String nodeName;

    private long startTime;

    private long time;

    private String streamName;

    private List<ProducerCounterModel> producers=new ArrayList<>();

    private List<ConsumerCounterModel> consumers=new ArrayList<>();

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

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

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public List<ProducerCounterModel> getProducers() {
        return producers;
    }

    public void setProducers(List<ProducerCounterModel> producers) {
        this.producers = producers;
    }

    public List<ConsumerCounterModel> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<ConsumerCounterModel> consumers) {
        this.consumers = consumers;
    }
}
