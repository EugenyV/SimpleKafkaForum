package com.mera.eugeny.volosenkov.simpleforum.forumkafka.app;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.getquery.ResultStatusValue;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ResultStatusCash extends ConcurrentHashMap<Long, ResultStatusValue> {
    private static AtomicLong queryCounter = new AtomicLong();
    public static  Long genQueryId()
    {
        return queryCounter.incrementAndGet();
    }
}
