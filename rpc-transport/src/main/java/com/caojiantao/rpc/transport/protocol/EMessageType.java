package com.caojiantao.rpc.transport.protocol;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * @author caojiantao
 */
@AllArgsConstructor
public enum EMessageType {


    ;

    public byte value;

    public static EMessageType ofValue(byte value) {
        for (EMessageType type : EMessageType.values()) {
            if (Objects.equals(value, type.value)) {
                return type;
            }
        }
        return null;
    }
}
