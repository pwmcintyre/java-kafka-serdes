package com.pwmcintyre.kafka.serde.base64;

import java.util.Map;
import java.util.Objects;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

/*
 * A Serde that wraps a Serializer and Deserializer with Base64 encoding and decoding.
 */
public class Base64SerDe<T> implements Serde<T> {

    private final Base64Serializer<T> serializer;
    private final Base64Deserializer<T> deserializer;

    public Base64SerDe() {
        this(new Base64Serializer<>(), new Base64Deserializer<>());
    }

    public Base64SerDe(Serde<T> s) {
        Objects.requireNonNull(s, "serde can't be null");
        serializer = new Base64Serializer<>(s.serializer());
        deserializer = new Base64Deserializer<>(s.deserializer());
    }

    public Base64SerDe(Serializer<T> s, Deserializer<T> d) {
        Objects.requireNonNull(s, "serializer can't be null");
        Objects.requireNonNull(d, "deserializer can't be null");
        serializer = new Base64Serializer<>(s);
        deserializer = new Base64Deserializer<>(d);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    @Override
    public Serializer<T> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<T> deserializer() {
        return deserializer;
    }

    @Override
    public void close() {
        serializer.close();
        deserializer.close();
    }

}
