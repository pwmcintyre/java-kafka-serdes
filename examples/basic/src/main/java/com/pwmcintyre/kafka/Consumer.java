package com.pwmcintyre.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.pwmcintyre.dto.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Consumer {

    public static void listen(Properties configs) {

        var topic = configs.getProperty("kafka.persons.topic.name");

        try (KafkaConsumer<String, Person> consumer = new KafkaConsumer<>(configs)) {

            consumer.subscribe(Arrays.asList(topic.split(",")));
            while (true) {
                ConsumerRecords<String, Person> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, Person> record : records) {
                    log.info("Received on {}: {}", topic, record.value());
                }
            }
        }
    }

}
