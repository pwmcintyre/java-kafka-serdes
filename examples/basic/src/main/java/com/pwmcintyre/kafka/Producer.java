package com.pwmcintyre.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.pwmcintyre.dto.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Producer {

    public static void start(Properties configs) {

        var topic = configs.getProperty("kafka.persons.topic.name");

        try (org.apache.kafka.clients.producer.Producer<String, Person> producer = new KafkaProducer<>(configs)) {
            for (char c = 'A';; c++) {

                // make a Person
                Person person = new Person(String.format("bar-%s", (char) ((c - 'A') % 26 + 'A')));
        
                // prepare record
                var rec = new ProducerRecord<>(topic, null, person.name(), person);
        
                // send
                producer.send(rec);
                log.info("sent to {}: {}", topic, person);

                // wait
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Could not start producer: " + e);
        }
    }

}
