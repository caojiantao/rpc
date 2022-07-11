package com.caojiantao.rpc.serialize.impl;

import com.caojiantao.rpc.serialize.ISerialization;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import java.io.IOException;

/**
 * @author caojiantao
 */
public class JsonSerialization implements ISerialization {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        // 序列化携带类描述信息
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @Override
    public <T> byte[] serialize(T data) throws IOException {
        return mapper.writeValueAsBytes(data);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        return mapper.readValue(data, clazz);
    }
}
