package me.libme.streaming.bigdata._trait;

import com.fasterxml.jackson.core.type.TypeReference;
import me.libme.fn.netty.server.HttpRequest;
import me.libme.kernel._c.cache.JMapCacheService;
import me.libme.kernel._c.json.JJSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J on 2018/11/19.
 */
public class ConsumerCountReporterInZK implements ConsumerCountReporter {

    private JMapCacheService<String,Long> counter=new JMapCacheService<>();

    private Map<String,ConsumerCounterModel> nodeConsumeCounters=new HashMap<>();

    private Map<String,ConsumerCounterModel> finalNodeConsumeCounters=new HashMap<>();

    @Override
    public boolean count(String consumerCountJson, HttpRequest httpRequest) {

        List<ConsumerCounterModel> consumerCounterModels= JJSON.get().parse(consumerCountJson,
                new TypeReference<List<ConsumerCounterModel>>() {});

        consumerCounterModels.forEach(consumerCounterModel -> {

            String key=consumerCounterModel.getNodeName();
            ConsumerCounterModel counterModel= nodeConsumeCounters.get(key);
            if(counterModel==null){

                nodeConsumeCounters.put(key,consumerCounterModel);
            }else if(counterModel.getStartTime()==consumerCounterModel.getStartTime()){

                if(counterModel.getTime()<consumerCounterModel.getTime()
                        &&counterModel.getCount()<consumerCounterModel.getCount()){
                    nodeConsumeCounters.put(key,consumerCounterModel);
                }
            }


        });




        return false;
    }


    private String key(ConsumerCounterModel consumerCounterModel){
        return consumerCounterModel.getNodeName()+"-"+consumerCounterModel.getConsumerName();
    }


}
