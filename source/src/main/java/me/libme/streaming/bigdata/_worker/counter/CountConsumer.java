package me.libme.streaming.bigdata._worker.counter;

import me.libme.xstream.Compositer;
import me.libme.xstream.ConsumerMeta;
import me.libme.xstream.Tupe;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by J on 2018/11/19.
 */
public class CountConsumer extends Compositer {

    private AtomicLong counter=null;

    public CountConsumer(ConsumerMeta consumerMeta) {
        super(consumerMeta);
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();
        counter=new AtomicLong(0);
    }

    @Override
    protected void prepare(Tupe tupe) {
        super.prepare(tupe);
        counter.incrementAndGet();
    }

    @Override
    protected void doConsume(Tupe tupe) throws Exception {

    }
}
