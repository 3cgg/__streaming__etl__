package me.libme.streaming.bigdata._worker.counter;

import me.libme.cls.cluster.Action;
import me.libme.cls.cluster.ClusterConfig;
import me.libme.cls.cluster.ClusterInfo;
import me.libme.cls.cluster.PathListenerClientFactory;
import me.libme.kernel._c.json.JJSON;
import me.libme.streaming.bigdata._trait.ConsumerCounterModel;
import me.libme.streaming.bigdata._trait.CounterReporter;
import me.libme.streaming.bigdata._trait.NodeCounterModel;
import me.libme.streaming.bigdata._trait.ProducerCounterModel;
import me.libme.xstream.fn.counter.ConsumerCountService;
import me.libme.xstream.fn.counter.ProducerCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by J on 2018/11/19.
 */
public class CounterSchedule implements Action {

    private static final Logger LOGGER= LoggerFactory.getLogger(CounterSchedule.class);

    private ScheduledExecutorService scheduledExecutorService= null;

    private ClusterConfig clusterConfig= null;

    private CounterReporter consumerCountReporter;

    private long startTime;

    @Override
    public void prepare() throws Exception {
        scheduledExecutorService= Executors.newScheduledThreadPool(1);
        clusterConfig= ClusterInfo.defaultConfig();
        consumerCountReporter= PathListenerClientFactory.factory(CounterReporter.class, CounterReporter.PATH);
        startTime=new Date().getTime();
    }

    @Override
    public void start() throws Exception {
        scheduledExecutorService.scheduleAtFixedRate(()->{

            try{
                long time=new Date().getTime();
                Map<String,NodeCounterModel> streams=new HashMap<>();

                Map<String,Long> consumerCount= ConsumerCountService.get().counter();
                consumerCount.forEach((key,val)->{
                    ConsumerCounterModel consumerCounterModel=new ConsumerCounterModel();
                    consumerCounterModel.setNodeName(clusterConfig.getWorker().getName());
                    consumerCounterModel.setConsumerName(ConsumerCountService.get().consumerName(key));
                    consumerCounterModel.setStreamName(ConsumerCountService.get().streamName(key));

                    consumerCounterModel.setCount(val.longValue());
                    consumerCounterModel.setTime(time);
                    consumerCounterModel.setStartTime(startTime);
                    if(!streams.containsKey(consumerCounterModel.getStreamName())){
                        NodeCounterModel nodeCounterModel=new NodeCounterModel();
                        nodeCounterModel.setNodeName(clusterConfig.getWorker().getName());
                        nodeCounterModel.setStartTime(startTime);
                        nodeCounterModel.setTime(time);
                        nodeCounterModel.setStreamName(consumerCounterModel.getStreamName());

                        nodeCounterModel.getConsumers().add(consumerCounterModel);
                        streams.put(consumerCounterModel.getStreamName(),nodeCounterModel);
                    }else {
                        streams.get(consumerCounterModel.getStreamName()).getConsumers().add(consumerCounterModel);
                    }
                });


                Map<String,Long> producerCount= ProducerCountService.get().counter();
                producerCount.forEach((key,val)->{
                    ProducerCounterModel producerCounterModel=new ProducerCounterModel();
                    producerCounterModel.setNodeName(clusterConfig.getWorker().getName());
                    producerCounterModel.setProducerName(ProducerCountService.get().producerName(key));
                    producerCounterModel.setStreamName(ProducerCountService.get().streamName(key));
                    producerCounterModel.setCount(val.longValue());
                    producerCounterModel.setTime(time);
                    producerCounterModel.setStartTime(startTime);
                    if(!streams.containsKey(producerCounterModel.getStreamName())){
                        NodeCounterModel nodeCounterModel=new NodeCounterModel();
                        nodeCounterModel.setNodeName(clusterConfig.getWorker().getName());
                        nodeCounterModel.setStartTime(startTime);
                        nodeCounterModel.setTime(time);
                        nodeCounterModel.setStreamName(producerCounterModel.getStreamName());
                        nodeCounterModel.getProducers().add(producerCounterModel);
                    }else {
                        streams.get(producerCounterModel.getStreamName()).getProducers().add(producerCounterModel);
                    }
                });
                consumerCountReporter.count(JJSON.get().format(streams.values()),null);
            }catch (Exception e){
                LOGGER.error(e.getMessage(),e);
            }
        },10,30, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown() throws Exception {
        scheduledExecutorService.shutdown();
    }





}
