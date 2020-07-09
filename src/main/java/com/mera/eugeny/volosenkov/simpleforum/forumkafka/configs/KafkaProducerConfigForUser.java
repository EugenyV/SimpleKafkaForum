package com.mera.eugeny.volosenkov.simpleforum.forumkafka.configs;


import com.mera.eugeny.volosenkov.simpleforum.forumkafka.data.User;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfigForUser {

    private String kafkaServer="localhost:9092";

    @Bean
    public Map<String, Object> producerConfigsLongUser() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<Long, User> producerFactoryLongUser() {
        return new DefaultKafkaProducerFactory<>(producerConfigsLongUser());
    }

    @Bean
    public KafkaTemplate<Long, User> kafkaTemplateLongUser() {
        KafkaTemplate<Long, User> res = new KafkaTemplate<>(producerFactoryLongUser());
        res.setMessageConverter(new StringJsonMessageConverter());
        return res;
    }
}