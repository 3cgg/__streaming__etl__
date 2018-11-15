package me.libme.streaming.bigdata._worker;

import me.libme.cls.cluster.Action;
import me.libme.xstream.stream.JSONFileStreamBuilder;
import me.libme.xstream.stream.Stream;

import java.io.InputStream;

/**
 * Created by J on 2018/11/15.
 */
public class StreamProcess implements Action {

    private InputStream inputStream;

    private Stream stream;

    public StreamProcess(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public StreamProcess(String file) {
        this(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(file));
    }

    @Override
    public void prepare() throws Exception {
        stream=new JSONFileStreamBuilder().build(inputStream);
    }

    @Override
    public void start() throws Exception {
        stream.start();
    }

    @Override
    public void shutdown() throws Exception {
        stream.shutdown();
    }


}
