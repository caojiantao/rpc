package com.caojiantao.rpc.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.SneakyThrows;

public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        // 序列化携带类描述信息
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @SneakyThrows
    public static <T> byte[] bytes(T data) {
        return mapper.writeValueAsBytes(data);
    }

    @SneakyThrows
    public static <T> T parse(byte[] data, Class<T> clazz) {
        return mapper.readValue(data, clazz);
    }
}
