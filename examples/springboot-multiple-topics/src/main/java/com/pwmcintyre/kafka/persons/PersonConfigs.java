package com.pwmcintyre.kafka.persons;

import java.util.HashMap;
import java.util.Map;

import com.pwmcintyre.kafka.CommonConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.pwmcintyre.dto.Person;
import com.pwmcintyre.kafka.serde.base64.Base64Deserializer;
import com.pwmcintyre.kafka.serde.base64.Base64SerDeConfig;
import com.pwmcintyre.kafka.serde.base64.Base64Serializer;

@Configuration
public class PersonConfigs extends CommonConfigs {

    @Value("${kafka.persons.consumer.group-id}")
    private String groupId;

    /**
     * Configures the properties for the persons topic.
     * You can add both consumer/producer configs here — eg. Producer will ignore Consumer configs.
     */
    public Map<String, Object> personsTopicConfig() {
        Map<String, Object> configs = kafkaConfig();
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Base64Serializer.class);
        configs.put(Base64SerDeConfig.SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Base64Deserializer.class);
        configs.put(Base64SerDeConfig.DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Person.class);
        return configs;
    }

    @Bean
    public ConsumerFactory<String, Person> personConsumerFactory() {
        Map<String, Object> configs = personsTopicConfig();
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Person> personKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Person> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(personConsumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, Person> personProducerFactory() {
        return new DefaultKafkaProducerFactory<>(personsTopicConfig());
    }

    @Bean KafkaTemplate<String, Person> personKafkaTemplate() {
        return new KafkaTemplate<>(personProducerFactory());
    }

}
