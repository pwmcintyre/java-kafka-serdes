package com.pwmcintyre.kafka.serde.base64;

import org.apache.kafka.common.Configurable;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Base64;
import java.util.Map;

public class Base64Deserializer<T> implements Deserializer<T>, Configurable {

    private Deserializer<T> inner;
    private boolean configured = false;

    /**
     * Empty args constructor used in conjunction with {@link #configure}
     */
    public Base64Deserializer() {
    }

    /**
     * Args constructor used for programmatic instantiation
     * @param inner used to deserialize data after decoding
     */
    public Base64Deserializer(Deserializer<T> inner) {
        this.inner = inner;
    }

    /**
     * Overrides {@link Deserializer#configure}
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        configure(configs);
        this.inner.configure(configs, isKey);
    }

    /**
     * Declares this class as Configurable, used by {@link AbstractConfig#getConfiguredInstance}
     */
    @Override
    public void configure(Map<String, ?> configs) {
        configure(new Base64SerDeConfig(configs));
    }

    /**
     * Apply configs declared by {@link Base64SerDeConfig}
     */
    @SuppressWarnings("unchecked")
    protected synchronized void configure(Base64SerDeConfig configs) {
        if (configured) return;
        this.inner = configs.getConfiguredInstance(Base64SerDeConfig.DESERIALIZER_CLASS_CONFIG, Deserializer.class);
        configured = true;
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
