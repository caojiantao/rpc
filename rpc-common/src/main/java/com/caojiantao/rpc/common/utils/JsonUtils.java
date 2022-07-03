package com.caojiantao.rpc.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static <T> byte[] bytes(T data) {
        return mapper.writeValueAsBytes(data);
    }

    @SneakyThrows
    public static <T> T parse(byte[] data, Class<T> clazz) {
        return mapper.readValue(data, clazz);
    }
}
