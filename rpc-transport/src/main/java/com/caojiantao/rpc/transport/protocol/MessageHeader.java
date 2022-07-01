package com.caojiantao.rpc.transport.protocol;

import com.caojiantao.rpc.transport.serialize.ESerializeType;
import lombok.Data;

/**
 * @author caojiantao
 */
@Data
public class MessageHeader {

    private Byte magic = Constants.MAGIC;
    private Long id;
    private Byte version = 1;
    private ESerializeType serialize;
    private EMessageType type;
    private Integer length;
}
