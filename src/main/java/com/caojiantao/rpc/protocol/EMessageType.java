package com.caojiantao.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author caojiantao
 */
@Getter
@AllArgsConstructor
public enum EMessageType {

    REQ((byte) 1),
    RESP((byte) 2),
    PING((byte) 3),
    PONG((byte) 4),
    ;

    private byte value;

    public static EMessageType ofValue(byte value) {
        for (EMessageType type : EMessageType.values()) {
            if (Objects.equals(value, type.value)) {
                return type;
            }
        }
        return null;
    }
}
