package com.pwmcintyre.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfigs {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Kafka cluster config
     */
    public Map<String, Object> kafkaConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return props;
    }

}
