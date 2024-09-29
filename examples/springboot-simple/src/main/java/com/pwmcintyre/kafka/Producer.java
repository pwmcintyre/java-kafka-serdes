package com.pwmcintyre.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pwmcintyre.dto.Person;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Producer {

    @Autowired
    private KafkaTemplate<String, Person> producer;

    @Value("${kafka.persons.topic.name}")
    private String topic;

    // for demo purposes, we'll send a sequence of Person objects
    private char c = 'A';

    // for demo purposes, regularly send kafka messages
    @Scheduled(fixedDelay = 1000)
    public void send() {

        // make a Person
        Person person = Person.builder()
                .name(String.format("bar-%s", (char) ((c++ - 'A') % 26 + 'A')))
                .build();

        // prepare record
        var rec = new ProducerRecord<>(topic, null, person.name, person);

        // send
        producer.send(rec);
        log.debug("sent to {}: {}", topic, person);
    }

}
