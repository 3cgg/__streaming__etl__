package me.libme.streaming.bigdata._worker.counter;

import me.libme.cls.cluster.Action;
import me.libme.cls.cluster.ClusterConfig;
import me.libme.cls.cluster.ClusterInfo;
import me.libme.cls.cluster.PathListenerClientFactory;
import me.libme.kernel._c.json.JJSON;
import me.libme.streaming.bigdata._trait.ConsumerCountReporter;
import me.libme.streaming.bigdata._trait.ConsumerCounterModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by J on 2018/11/19.
 */
public class CounterSchedule implements Action {


    private ScheduledExecutorService scheduledExecutorService= null;

    private ClusterConfig clusterConfig= null;

    private ConsumerCountReporter consumerCountReporter;

    private long startTime;

    @Override
    public void prepare() throws Exception {
        scheduledExecutorService= Executors.newScheduledThreadPool(1);
        clusterConfig= ClusterInfo.defaultConfig();
        consumerCountReporter= PathListenerClientFactory.factory(ConsumerCountReporter.class,ConsumerCountReporter.PATH);
        startTime=new Date().getTime();
    }

    @Override
    public void start() throws Exception {
        scheduledExecutorService.scheduleAtFixedRate(()->{

            Map<String,Long> consumerCount= CounterService.get().consumerCount();

            List<ConsumerCounterModel> consumerCounterModels=new ArrayList<>();
            consumerCount.forEach((key,val)->{
                ConsumerCounterModel consumerCounterModel=new ConsumerCounterModel();
                consumerCounterModel.setNodeName(clusterConfig.getWorker().getName());
                consumerCounterModel.setConsumerName(key);
                consumerCounterModel.setCount(val.longValue());
                consumerCounterModel.setTime(new Date().getTime());
                consumerCounterModel.setStartTime(startTime);
                consumerCounterModels.add(consumerCounterModel);
            });
            consumerCountReporter.count(JJSON.get().format(consumerCounterModels),null);
        },10,30, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown() throws Exception {
        scheduledExecutorService.shutdown();
    }



}
