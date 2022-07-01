package com.caojiantao.rpc.transport.serialize;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * @author caojiantao
 */
@AllArgsConstructor
public enum ESerializeType {

    JSON((byte) 1, JsonSerialization.class),
    ;

    public byte value;
    public Class<? extends ISerialization> clazz;

    public static ESerializeType ofValue(byte type) {
        for (ESerializeType serialize : ESerializeType.values()) {
            if (Objects.equals(type, serialize.value)) {
                return serialize;
            }
        }
        return null;
    }
}
