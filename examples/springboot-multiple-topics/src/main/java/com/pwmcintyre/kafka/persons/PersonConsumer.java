package com.pwmcintyre.kafka.persons;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.pwmcintyre.dto.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Configuration
public class PersonConsumer {

    @KafkaListener(topics = "${kafka.persons.topic.name}", containerFactory = "personKafkaListenerContainerFactory")
    public void listen(final Person person, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.debug("Received on {}: {}", topic, person);
    }

}
