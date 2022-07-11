package com.caojiantao.rpc.serialize;

import com.caojiantao.rpc.serialize.impl.JsonSerialization;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author caojiantao
 */
@Getter
@AllArgsConstructor
public enum ESerializeType {

    JSON((byte) 1, new JsonSerialization()),
    ;

    private byte value;
    private ISerialization serialization;

    public static ESerializeType ofValue(byte type) {
        for (ESerializeType serialize : ESerializeType.values()) {
            if (Objects.equals(type, serialize.value)) {
                return serialize;
            }
        }
        return null;
    }
}
