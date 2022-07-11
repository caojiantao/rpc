package com.caojiantao.rpc.serialize;

import java.io.IOException;

/**
 * @author caojiantao
 */
public interface ISerialization {

    <T> byte[] serialize(T data) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clazz) throws IOException;
}
