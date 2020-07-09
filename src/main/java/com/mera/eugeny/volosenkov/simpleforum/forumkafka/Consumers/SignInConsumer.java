package com.mera.eugeny.volosenkov.simpleforum.forumkafka.Consumers;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.Producers.CheckUserProducer;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.Producers.GenUserSessionProducer;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.AppData;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.getquery.ResultStatusValue;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.*;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers.MySerializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SignInConsumer {
    private final Logger logger = (Logger) LoggerFactory.getLogger(SignInConsumer.class);
    private static final String LOG_HEADER_TEMPLATE = String.format("#### -> %s message", SignInConsumer.class.getName());
    private CheckUserProducer checkUserProducer;
    private GenUserSessionProducer genUserSessionProducer;

    public SignInConsumer(CheckUserProducer checkUserProducer, GenUserSessionProducer genUserSessionProducer) {
        this.checkUserProducer = checkUserProducer;
        this.genUserSessionProducer = genUserSessionProducer;
    }

    @KafkaListener(topics="auth_user", containerFactory= "singleUserFactory")
    public void authUser(ConsumerRecord<Long, String> record) throws IOException, InterruptedException
    {
        Long resultQueryId= (Long)record.key();
        User user = (User) MySerializer.Deserialize(record.value(), User.class);

        String login = user.getLogin();
        logger.info(String.format("%s -> Attempt to authorize user with login %s", LOG_HEADER_TEMPLATE, login));

        Long checkUserQueryID = checkUserProducer.sendMessage(login);
        Thread.sleep(5);
        while(!AppData.resultStatusTab.get(checkUserQueryID).isConsumed())
        {
            Thread.sleep(5);
        }
        CheckUserResult checkUserRes = (CheckUserResult)AppData.resultStatusTab.get(checkUserQueryID).getResult();
        AppData.resultStatusTab.remove(checkUserQueryID);

        if(checkUserRes.getStatus()== CheckUserresultStatus.USER_IS_EXIST)
        {
            Long sessionResultID = genUserSessionProducer.sendMessage(login);
            Thread.sleep(5);
            while(!AppData.resultStatusTab.get(sessionResultID).isConsumed())
            {
                Thread.sleep(5);
            }
            SessionResult resSession = (SessionResult)AppData.resultStatusTab.get(sessionResultID).getResult();
            AppData.resultStatusTab.remove(sessionResultID);
            AppData.resultStatusTab.put(resultQueryId, new ResultStatusValue(true, resSession));
        }
        else
        {
            AppData.resultStatusTab.put(resultQueryId,
                    new ResultStatusValue( true, new RegUserResult(UserResultStatus.USER_IS_NOT_EXISTS)));
        }
    }
}
