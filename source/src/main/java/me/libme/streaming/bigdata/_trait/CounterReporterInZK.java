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
                List<ConsumerCounterModel> consumerCounterModels=new ArrayList<>();
                nodeConsumeCounters.keys().forEach((key)->{

                    ConsumerCounterModel consumerCounterModel=nodeConsumeCounters.get(key);
                    consumerCounterModels.add(consumerCounterModel);
                });

                executor.createPath("/report/fn/counter", JJSON.get().format(consumerCounterModels));

            },10,120, TimeUnit.SECONDS);
        });
    }

    @Override
    public boolean count(String countJson, HttpRequest httpRequest) {

        List<NodeCounterModel> nodeCounterModels= JJSON.get().parse(countJson,
                new TypeReference<List<NodeCounterModel>>() {});

        nodeCounterModels.forEach(nodeCounterModel -> {

            String key=key(nodeCounterModel);
            NodeCounterModel counterModel= nodeConsumeCounters.get(key);
            if(counterModel==null){
                nodeConsumeCounters.expire(key,nodeCounterModel,90, TimeUnit.SECONDS);
            }else if(counterModel.getTime()<nodeCounterModel.getTime()){
                nodeConsumeCounters.expire(key,nodeCounterModel,90, TimeUnit.SECONDS);
            }
        });
        return true;
    }


    private String key(NodeCounterModel nodeCounterModel){
        return nodeCounterModel.getStreamName()+"-"+nodeCounterModel.getNodeName()+"-"+nodeCounterModel.getStartTime();
    }


}
