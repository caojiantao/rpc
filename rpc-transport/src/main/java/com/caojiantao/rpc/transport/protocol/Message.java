package com.caojiantao.rpc.transport.protocol;

import lombok.Data;

/**
 * @author caojiantao
 */
@Data
public class Message<T> {

    private MessageHeader header;
    private T body;
}
