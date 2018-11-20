package me.libme.streaming.bigdata._trait;

import com.fasterxml.jackson.core.type.TypeReference;
import me.libme.fn.netty.server.HttpRequest;
import me.libme.fn.netty.server.fn._dispatch.PathListenerInitializeQueue;
import me.libme.kernel._c.cache.JMapCacheService;
import me.libme.kernel._c.json.JJSON;
import me.libme.module.zookeeper.ZKExecutor;
import me.libme.module.zookeeper.ZooKeeperConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by J on 2018/11/19.
 */
public class CounterReporterInZK implements CounterReporter {

    /**
     * key :  streamName-nodeName-startTime
     */
    private JMapCacheService<String,NodeCounterModel> nodeConsumeCounters=new JMapCacheService<>(true);

    private Map<String,ConsumerCounterModel> finalNodeConsumeCounters=new HashMap<>();

    private ScheduledExecutorService scheduledExecutorService= null;

    private ZooKeeperConnector.ZookeeperExecutor executor;

    public CounterReporterInZK() {
        PathListenerInitializeQueue.get().offer(()->{
            executor= ZKExecutor.defaultExecutor();
            scheduledExecutorService= Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleAtFixedRate(()->{
                Map<String,List<NodeCounterModel>> map=new HashMap<>();
                nodeConsumeCounters.keys().forEach((key)->{
                    String streamName=key.split("-")[0];
                    if(!map.containsKey(streamName)){
                        List<NodeCounterModel> nodeCounterModels=new ArrayList<>();
                        nodeCounterModels.add(nodeConsumeCounters.get(key));
                        map.put(streamName,nodeCounterModels);
                    }else{
                        map.get(streamName).add(nodeConsumeCounters.get(key));
                    }
                });
                map.forEach((streamName,list)->{
                    String streamPath="/report/fn/counter/"
                            +streamName.replace(" ","-").replace("/","-");
                    if(executor.exists(streamPath)){
                        executor.setPath(streamPath,JJSON.get().format(list));
                    }else {
                        executor.createPath(streamPath,JJSON.get().format(list));
                    }
                });
            },10,120, TimeUnit.SECONDS);
        });
    }

    @Override
    public boolean count(String countJson, HttpRequest httpRequest) {

        List<NodeCounterModel> nodeCounterModels= JJSON.get().parse(countJson,
                new TypeReference<List<NodeCounterModel>>() {});

        nodeCounterModels.forEach(nodeCounterModel -> {

            String key=key(nodeCounterModel);
            NodeCounterModel previousCounterModel= nodeConsumeCounters.get(key);
            if(previousCounterModel==null){
                nodeConsumeCounters.expire(key,nodeCounterModel,90, TimeUnit.SECONDS);
            }else if(previousCounterModel.getTime()<nodeCounterModel.getTime()){
                nodeConsumeCounters.expire(key,nodeCounterModel,90, TimeUnit.SECONDS);
            }
        });
        return true;
    }


    private String key(NodeCounterModel nodeCounterModel){
        return nodeCounterModel.getStreamName()+"-"+nodeCounterModel.getNodeName()+"-"+nodeCounterModel.getStartTime();
    }


}
