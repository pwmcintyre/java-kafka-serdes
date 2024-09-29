package com.pwmcintyre.kafka.serde.base64;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class Base64SerDeConfig extends AbstractConfig {

    public static final String DESERIALIZER_CLASS_CONFIG = "base64.inner.deserializer";
    public static final String DESERIALIZER_CLASS_CONFIG_DEFAULT = "org.apache.kafka.common.serialization.ByteArrayDeserializer";
    public static final String DESERIALIZER_CLASS_CONFIG_DOC = "The class which is used to deserialize after decoding";

    public static final String SERIALIZER_CLASS_CONFIG = "base64.inner.serializer";
    public static final String SERIALIZER_CLASS_CONFIG_DEFAULT = "org.apache.kafka.common.serialization.ByteArraySerializer";
    public static final String SERIALIZER_CLASS_CONFIG_DOC = "The class which is used to serialize before encoding";

    private static final ConfigDef definition = new ConfigDef()
            .define(
                    DESERIALIZER_CLASS_CONFIG,
                    ConfigDef.Type.CLASS,
                    DESERIALIZER_CLASS_CONFIG_DEFAULT,
                    ConfigDef.Importance.HIGH,
                    DESERIALIZER_CLASS_CONFIG_DOC)
            .define(
                    SERIALIZER_CLASS_CONFIG,
                    ConfigDef.Type.CLASS,
                    SERIALIZER_CLASS_CONFIG_DEFAULT,
                    ConfigDef.Importance.HIGH,
                    SERIALIZER_CLASS_CONFIG_DOC);

    public Base64SerDeConfig(Map<?, ?> config) {
        super(definition, config);
    }

}
