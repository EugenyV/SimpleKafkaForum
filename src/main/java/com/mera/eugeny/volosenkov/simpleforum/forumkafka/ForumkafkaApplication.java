package com.mera.eugeny.volosenkov.simpleforum.forumkafka;

import com.mera.eugeny.volosenkov.simpleforum.forumkafka.app.AppData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

import java.io.IOException;

@SpringBootApplication
@EnableKafka
public class ForumkafkaApplication {

	public static void main(String[] args) throws IOException {
		AppData.init();
		SpringApplication.run(ForumkafkaApplication.class, args);
	}

}
