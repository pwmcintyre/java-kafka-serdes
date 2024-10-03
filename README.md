# Kafka SerDes

This project demonstrates how to build custom SerDes for Kafka.

## Base64 SerDe

The base64 SerDe wraps another SerDe and encodes/decodes the serialized bytes using Base64 encoding.

```mermaid
graph LR
    Producer -->|publish| Kafka -->|listen| Consumer

    subgraph Kafka
        Broker1 <-->|sync messages| Broker2
    end

    subgraph Producer
        Object -->|serialize| JSONSerializer -->|encode| Base64Serializer
    end

    subgraph Consumer
        Base64Deserializer -->|decode| JSONDeserializer -->|deserialize| Object*
    end
```

## Local Kafka

Confluent provide several convenient options to run Kafka locally via Docker and Docker Compose.

https://github.com/confluentinc/cp-all-in-one

```shell
docker compose up -d
```

## Maintananace

```shell
mvn clean install
```
