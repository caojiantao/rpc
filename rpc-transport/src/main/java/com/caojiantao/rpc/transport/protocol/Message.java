package com.caojiantao.rpc.transport.protocol;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author caojiantao
 */
@Data
public class Message<T> {

    private MessageHeader header;
    @JsonIgnore
    private byte[] bytes;
    private T body;
}
