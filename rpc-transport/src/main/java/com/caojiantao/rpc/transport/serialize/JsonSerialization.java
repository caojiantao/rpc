package com.caojiantao.rpc.transport.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author caojiantao
 */
public class JsonSerialization implements ISerialization {

    private ObjectMapper mapper;

    @Override
    public <T> byte[] serialize(T data) throws IOException {
        return mapper.writeValueAsBytes(data);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        return mapper.readValue(data, clazz);
    }
}
