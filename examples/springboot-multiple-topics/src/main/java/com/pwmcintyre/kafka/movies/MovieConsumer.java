package com.pwmcintyre.kafka.movies;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.pwmcintyre.dto.Movie;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Configuration
public class MovieConsumer {

    @KafkaListener(topics = "${kafka.movies.topic.name}", containerFactory = "movieKafkaListenerContainerFactory")
    public void listen(final Movie movie, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.debug("Received on {}: {}", topic, movie);
    }

}
