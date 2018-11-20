package me.libme.streaming.bigdata._trait;

import me.libme.kernel._c._m.JModel;
import me.libme.kernel._c.json.JJSONObject;

import java.util.Map;

/**
 * Created by J on 2018/11/19.
 */
public class CounterModel implements JModel,JJSONObject<Map>{


    private String nodeName;

    private String streamName;

    private long startTime;

    private long time;

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

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

    @Override
    public Map serializableJSONObject() {
        return null;
    }
}
