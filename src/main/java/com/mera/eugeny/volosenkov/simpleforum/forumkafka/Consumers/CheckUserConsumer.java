package com.mera.eugeny.volosenkov.simpleforum.forumkafka.Consumers;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.Producers.CheckUserProducer;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.AppData;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.getquery.ResultStatusValue;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.CheckUserResult;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.CheckUserresultStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CheckUserConsumer {

    private final Logger logger = (Logger) LoggerFactory.getLogger(CheckUserConsumer.class);
    private static final String LOG_HEADER_TEMPLATE = String.format("#### -> %s message", CheckUserConsumer.class.getName());

    public CheckUserConsumer() {
    }

    @KafkaListener(topics="check_user", containerFactory = "kafkaListenerContainerFactoryLongString")
    public void checkUser( ConsumerRecord<Long, String> record) throws IOException {
        String login = record.value();
        Long resultQueryId= record.key();
        logger.info(String.format("%s -> Trying to check login %s", LOG_HEADER_TEMPLATE, login));
        // Do something
        System.out.println(record.partition());
        System.out.println(record.key());
        System.out.println(record.value());

        User res = AppData.usersTab.findUser(login);
        if(res!=null)
        {
            logger.info(String.format("%s -> User with name %s already exists!!!", LOG_HEADER_TEMPLATE, login));
            CheckUserResult queryRes = new CheckUserResult(CheckUserresultStatus.USER_IS_EXIST);
            AppData.resultStatusTab.put(resultQueryId, new ResultStatusValue(true, queryRes));
        }
        else
        {
            logger.info(String.format("%s -> User with name %s is not exist!!!", LOG_HEADER_TEMPLATE, login));
            logger.info(String.format("%s -> User with name %s will be added in UsersBD!!!", LOG_HEADER_TEMPLATE, login));
            CheckUserResult queryRes = new CheckUserResult(CheckUserresultStatus.USER_IS_NOT_EXIST);
            AppData.resultStatusTab.put(resultQueryId, new ResultStatusValue(true, queryRes));
            logger.info(String.format("%s -> User with name %s was added in UsersBD!!!", LOG_HEADER_TEMPLATE, login));
        }
    }

}
