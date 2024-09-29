package com.pwmcintyre.kafka.serde.base64;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Base64;
import java.util.Map;

public class Base64Deserializer<T> implements Deserializer<T> {

    private Deserializer<T> inner;

    // empty args constructor used in conjunction with configure()
    public Base64Deserializer() {
    }

    public Base64Deserializer(Deserializer<T> i) {
        inner = i;
    }

    @Override
    public synchronized void configure(Map<String, ?> configs, boolean isKey) {
        configure(new Base64SerDeConfig(configs), isKey);
    }

    @SuppressWarnings("unchecked")
    protected void configure(Base64SerDeConfig configs, boolean isKey) {
        this.inner = configs.getConfiguredInstance(Base64SerDeConfig.DESERIALIZER_CLASS_CONFIG, Deserializer.class);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        return deserialize(topic, null, data);
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {

        if (data == null) {
            return null;
        }

        // decode
        byte[] decoded = Base64.getDecoder().decode(data);

        // inner deserialize
        return inner.deserialize(topic, headers, decoded);
    }

    @Override
    public void close() {
        if (inner != null) {
            inner.close();
        }
    }
}
