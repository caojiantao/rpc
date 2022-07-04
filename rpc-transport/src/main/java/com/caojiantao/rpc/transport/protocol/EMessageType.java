package com.caojiantao.rpc.transport.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author caojiantao
 */
@Getter
@AllArgsConstructor
public enum EMessageType {

    HEART((byte) 1),
    REQUEST((byte) 2),
    RESPONSE((byte) 3),
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
