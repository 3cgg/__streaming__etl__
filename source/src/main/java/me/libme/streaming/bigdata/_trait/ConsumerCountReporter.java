package me.libme.streaming.bigdata._trait;

import me.libme.fn.netty.server.HttpRequest;
import me.libme.fn.netty.server.fn._dispatch.PathListener;

/**
 * Created by J on 2018/11/19.
 */
public interface ConsumerCountReporter extends PathListener {

    String PATH="/node/status/report";

    boolean count(String consumerCountMapJson,HttpRequest httpRequest);

}
