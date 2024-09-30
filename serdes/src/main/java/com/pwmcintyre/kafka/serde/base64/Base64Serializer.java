package com.pwmcintyre.kafka.serde.base64;

import java.util.Base64;
import java.util.Map;

import org.apache.kafka.common.Configurable;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

public class Base64Serializer<T> implements Serializer<T>, Configurable {

    private Serializer<T> inner;
    private boolean configured = false;

    /**
     * Empty args constructor used in conjunction with {@link #configure}
     */
    public Base64Serializer() {
    }

    /**
     * Args constructor used for programmatic instantiation
     * @param inner used to serialize data prior to encoding
     */
    public Base64Serializer(Serializer<T> inner) {
        this.inner = inner;
    }

    /**
     * Overrides {@link Serializer#configure}
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
        this.inner = configs.getConfiguredInstance(Base64SerDeConfig.SERIALIZER_CLASS_CONFIG, Serializer.class);
        configured = true;
    }

    /**
     * @param topic topic associated with data
     * @param data typed data
     * @return Base64 encoded serialized data
     */
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