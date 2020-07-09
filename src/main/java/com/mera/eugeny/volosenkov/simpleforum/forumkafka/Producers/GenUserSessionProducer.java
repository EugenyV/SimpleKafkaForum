package com.mera.eugeny.volosenkov.simpleforum.forumkafka.Producers;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.AppData;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.ResultStatusCash;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.getquery.ResultStatusValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class GenUserSessionProducer {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(GenUserSessionProducer.class);
    private static final String TOPIC = "gen_user_session";
    private static final String LOG_HEADER_TEMPLATE = String.format("#### -> %s message", GenUserSessionProducer.class.getName());

    public GenUserSessionProducer(@Qualifier("kafkaTemplateLongString") KafkaTemplate<Long, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    @Qualifier("kafkaTemplateLongString")
    private KafkaTemplate<Long, String> kafkaTemplate;

    public Long sendMessage(String login) {
        logger.info(String.format("%s -> Trying to gen sessionID user with login %s", LOG_HEADER_TEMPLATE, login));
        Long queryID = ResultStatusCash.genQueryId();
        AppData.resultStatusTab.put(queryID, new ResultStatusValue(false, null));
        ListenableFuture<SendResult<Long, String>> future = kafkaTemplate.send(TOPIC, queryID, login);
        future.addCallback(System.out::println, System.err::println);
        kafkaTemplate.flush();
        return queryID;
    }
}
