package com.caojiantao.rpc.transport.serialize;

import com.caojiantao.rpc.common.utils.JsonUtils;

import java.io.IOException;

/**
 * @author caojiantao
 */
public class JsonSerialization implements ISerialization {

    @Override
    public <T> byte[] serialize(T data) throws IOException {
        return JsonUtils.bytes(data);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        return JsonUtils.parse(data, clazz);
    }
}
