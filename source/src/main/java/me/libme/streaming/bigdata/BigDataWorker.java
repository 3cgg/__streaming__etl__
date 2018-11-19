package me.libme.streaming.bigdata;

import me.libme.cls.cluster.SimpleWorker;
import me.libme.streaming.bigdata._worker.counter.CounterSchedule;

/**
 * Created by J on 2018/11/15.
 */
public class BigDataWorker  {

    public static SimpleWorker.SimpleWorkerBuilder builder(){
        return new SimpleWorker.SimpleWorkerBuilder()
                .addAction(new CounterSchedule());
    }



}
