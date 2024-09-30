package com.pwmcintyre.kafka.movies;

import com.pwmcintyre.dto.Movie;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MovieProducer {

    @Autowired
    private KafkaTemplate<String, Movie> producer;

    @Value("${kafka.movies.topic.name}")
    private String topic;

    // for demo purposes, we'll send a sequence of Person objects
    private char c = 'A';

    // for demo purposes, regularly send kafka messages
    @Scheduled(fixedDelay = 1000)
    public void send() {

        // make a Person
        Movie movie = Movie.builder()
                .name(String.format("movie-%s", (char) ((c - 'A') % 26 + 'A')))
                .producer(String.format("producer-%s", (char) ((c++ - 'A') % 26 + 'A')))
                .build();

        // prepare record
        var rec = new ProducerRecord<>(topic, null, movie.name, movie);

        // send
        producer.send(rec);
        log.debug("sent to {}: {}", topic, movie);
    }

}
