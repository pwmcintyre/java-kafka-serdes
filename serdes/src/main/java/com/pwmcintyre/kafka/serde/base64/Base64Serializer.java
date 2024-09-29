package com.pwmcintyre.kafka.serde.base64;

import java.util.Base64;
import java.util.Map;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

public class Base64Serializer<T> implements Serializer<T> {

    private Serializer<T> inner;

    // empty args constructor used in conjunction with configure()
    public Base64Serializer() {
    }

    // args constructor used for programmatic instantiation
    public Base64Serializer(Serializer<T> i) {
        inner = i;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        configure(new Base64SerDeConfig(configs), isKey);
    }

    @SuppressWarnings("unchecked")
    protected void configure(Base64SerDeConfig configs, boolean isKey) {
        this.inner = configs.getConfiguredInstance(Base64SerDeConfig.SERIALIZER_CLASS_CONFIG, Serializer.class);
    }

    @Override
    public byte[] serialize(String topic, T data) {
        return serialize(topic, null, data);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, T data) {

        if (data == null) {
            return null;
        }

        // use inner serializer first
        byte[] serialized = inner.serialize(topic, data);

        // encode
        return Base64.getEncoder().encode(serialized);
    }

    @Override
    public void close() {
        if (inner != null) {
            inner.close();
        }
    }
}