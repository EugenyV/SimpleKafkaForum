package com.mera.eugeny.volosenkov.simpleforum.forumkafka.Producers;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.AppData;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.ResultStatusCash;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.getquery.ResultStatusValue;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class RegUserProducer extends ProducerBase{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(RegUserProducer.class);
    private static final String TOPIC = "reg_user";
    private static final String LOG_HEADER_TEMPLATE = String.format("#### -> %s message", RegUserProducer.class.getName());

    public RegUserProducer(@Qualifier("kafkaTemplateLongUser" ) KafkaTemplate<Long, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    @Qualifier("kafkaTemplateLongUser")
    private KafkaTemplate<Long, User> kafkaTemplate;

    public QueryResult sendMessage(String login, String password) {
        logger.info(String.format("%s -> Trying to reg user with login %s", LOG_HEADER_TEMPLATE, login));
        Long queryID = ResultStatusCash.genQueryId();
        AppData.resultStatusTab.put(queryID, new ResultStatusValue(false, null));
        ListenableFuture<SendResult<Long, User>> future = kafkaTemplate.send(TOPIC, queryID, new User(login, password));
        future.addCallback(System.out::println, System.err::println);
        kafkaTemplate.flush();

        return new QueryResult(queryID);
    }
}
