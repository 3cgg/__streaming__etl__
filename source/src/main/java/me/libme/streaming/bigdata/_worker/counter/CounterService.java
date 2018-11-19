package me.libme.streaming.bigdata._worker.counter;

import me.libme.kernel._c.cache.JMapCacheService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by J on 2018/11/19.
 */
public class CounterService {

    private static final CounterService COUNTER_SERVICE=new CounterService();

    private JMapCacheService<String,AtomicLong> counter=new JMapCacheService<>();


    public static CounterService get(){
        return COUNTER_SERVICE;
    }

    public synchronized void register(String consumerName){
        if(!counter.contains(consumerName)){
            AtomicLong atomicLong=new AtomicLong(0);
            counter.put(consumerName,atomicLong);
        }

    }


    public void increment(String consumerName){
        counter.get(consumerName).incrementAndGet();
    }

    Map<String,Long> consumerCount(){
        Map<String,Long> consumerCount=new HashMap<>();
        counter.keys().forEach(key->consumerCount.put(key,counter.get(key).longValue()));
        return consumerCount;
    }




}
