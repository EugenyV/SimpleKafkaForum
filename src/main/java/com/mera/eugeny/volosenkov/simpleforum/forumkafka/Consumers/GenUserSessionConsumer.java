package com.mera.eugeny.volosenkov.simpleforum.forumkafka.Consumers;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.Producers.CheckUserProducer;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.AppData;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.ActiveUser;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.getquery.ResultStatusValue;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.SessionResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GenUserSessionConsumer {
    private final Logger logger = (Logger) LoggerFactory.getLogger(GenUserSessionConsumer.class);
    private static final String LOG_HEADER_TEMPLATE = String.format("#### -> %s message", GenUserSessionConsumer.class.getName());
    @KafkaListener(topics = "gen_user_session", containerFactory = "kafkaListenerContainerFactoryLongString")
    public void genSession(ConsumerRecord<Long, String> record) throws IOException {
        String login = record.value();
        Long resultQueryId= record.key();
        logger.info(String.format("%s -> Trying to check login %s", LOG_HEADER_TEMPLATE, login));
        // Do something
        System.out.println(record.partition());
        System.out.println(record.key());
        System.out.println(record.value());
        String sessionId = AppData.activeUsers.getSessionID();
        ActiveUser activeUser = new ActiveUser(login, sessionId);
        AppData.activeUsers.addActiveUser(activeUser);
        AppData.resultStatusTab.put(resultQueryId, new ResultStatusValue(true, new SessionResult(sessionId)));
    }
}
