package com.mera.eugeny.volosenkov.simpleforum.forumkafka.Consumers;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.Producers.CheckUserProducer;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.AppData;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.getquery.ResultStatusValue;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.CheckUserResult;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.CheckUserresultStatus;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.RegUserResult;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.results.UserResultStatus;
import com.mera.eugeny.volosenkov.simpleforum.forumkafka.serializers.MySerializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RegUserConsumer {
    private final Logger logger = (Logger) LoggerFactory.getLogger(RegUserConsumer.class);
    private static final String LOG_HEADER_TEMPLATE = String.format("#### -> %s message", RegUserConsumer.class.getName());

    public RegUserConsumer(CheckUserProducer checkUserProducer) {
        this.checkUserProducer = checkUserProducer;
    }

    private CheckUserProducer checkUserProducer;
    @KafkaListener(topics="reg_user", containerFactory= "singleUserFactory")
    public void regUser(ConsumerRecord<Long, String> record) throws IOException, InterruptedException {
        Long resultQueryId= (Long)record.key();
        User user = (User)MySerializer.Deserialize(record.value(), User.class);

        String login = user.getLogin();

        logger.info(String.format("%s -> Trying to reg user with login %s", LOG_HEADER_TEMPLATE, login));
        // Do something
        System.out.println(record.partition());
        System.out.println(record.key());
        System.out.println(record.value());
        logger.info(String.format("%s -> Trying to check user with login %s", LOG_HEADER_TEMPLATE, login));
        Long checkUserQueryID = checkUserProducer.sendMessage(login);

        while(!AppData.resultStatusTab.get(checkUserQueryID).isConsumed())
        {
            Thread.sleep(5);
        }
        CheckUserResult checkUserRes = (CheckUserResult)AppData.resultStatusTab.get(checkUserQueryID).getResult();
        if(checkUserRes.getStatus()==CheckUserresultStatus.USER_IS_EXIST)
        {
            AppData.resultStatusTab.put(resultQueryId,
                    new ResultStatusValue( true, new RegUserResult(UserResultStatus.USER_EXIST_ALREADY)));
        }
        else
        {
            AppData.usersTab.addUser(user);
            AppData.check_update();
            AppData.resultStatusTab.put(resultQueryId,
                    new ResultStatusValue( true, new RegUserResult(UserResultStatus.OK)));
        }
    }
}
