package com.caojiantao.rpc.protocol;

import lombok.Data;

/**
 * @author caojiantao
 */
@Data
public class Message<T> {

    private MessageHeader header;
    private T body;
}
