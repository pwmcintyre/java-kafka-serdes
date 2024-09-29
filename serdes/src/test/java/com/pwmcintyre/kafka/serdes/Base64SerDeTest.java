package com.pwmcintyre.kafka.serdes;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pwmcintyre.kafka.serde.base64.Base64SerDe;
import com.pwmcintyre.kafka.serde.base64.Base64SerDeConfig;

public class Base64SerDeTest {

    public static final String TOPIC = "topic";

    // original message to be serialized
    public static final byte[] ORIGINAL = "Hello, World!".getBytes();

    // base64 encoded bytes of the original message
    public static final byte[] ORIGINALB64 = "SGVsbG8sIFdvcmxkIQ==".getBytes();

    @Test
    public void testSerDe() {

        // GIVEN an inner serde
        Serde<byte[]> inner = new Serdes.ByteArraySerde();

        // AND a base64 serializer
        Serde<byte[]> base64SerDe = new Base64SerDe<>(inner);

        // WHEN the object is serialized
        byte[] got = base64SerDe.serializer().serialize(TOPIC, ORIGINAL);

        // THEN the serialized object is the base64 encoded bytes of the inner serializer
        assertArrayEquals(ORIGINALB64, got);

        // WHEN the object is deserialized
        byte[] got2 = base64SerDe.deserializer().deserialize(TOPIC, got);

        // THEN the deserialized object is the original message
        assertArrayEquals(ORIGINAL, got2);

    }

    @Test
    public void testNullData() {

        // GIVEN a base64 serializer
        Serde<byte[]> base64SerDe = new Base64SerDe<>();

        // WHEN null is serialized
        byte[] got = base64SerDe.serializer().serialize(TOPIC, null);

        // THEN null is returned
        assertArrayEquals(null, got);

        // WHEN null is deserialized
        byte[] got2 = base64SerDe.deserializer().deserialize(TOPIC, got);

        // THEN null is returned
        assertArrayEquals(null, got2);

    }

    @Test
    public void testConfigure() {

        // GIVEN an unconfigured base64 serde
        Serde<byte[]> base64SerDe = new Base64SerDe<>();

        // AND a configuration indicating the inner serde to be ByteArraySerializer
        Map<String, Object> configs = new HashMap<>();
        configs.put(Base64SerDeConfig.SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        configs.put(Base64SerDeConfig.DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

        // WHEN the serde is configured
        base64SerDe.configure(configs, false);

        // AND the object is serialized
        byte[] got = base64SerDe.serializer().serialize(TOPIC, ORIGINAL);

        // THEN the serialized object is the base64 encoded bytes of the inner serializer
        assertArrayEquals(ORIGINALB64, got);

        // WHEN the object is deserialized
        byte[] got2 = base64SerDe.deserializer().deserialize(TOPIC, got);

        // THEN the deserialized object is the original message
        assertArrayEquals(ORIGINAL, got2);
    }

    @Test
    public void testClose() {

        // GIVEN a base64 SerDe with mocked inner serdes
        Serializer a = Mockito.mock(Serializer.class);
        Deserializer b = Mockito.mock(Deserializer.class);
        Serde s = new Base64SerDe(a, b);

        // WHEN close is called
        s.close();

        // THEN close is called on the inner serdes
        Mockito.verify(a).close();
        Mockito.verify(b).close();
    }

    @Test
    public void testNullClose() {

        // GIVEN a base64 serde with no inner serde
        Serde s = new Base64SerDe();

        // THEN close does not throw an exception
        assertDoesNotThrow(s::close);
    }

}