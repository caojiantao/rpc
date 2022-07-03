package com.caojiantao.rpc.transport.protocol;

import com.caojiantao.rpc.transport.serialize.ESerializeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author caojiantao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeader {

    @Builder.Default
    private Byte magic = Constants.MAGIC;
    @Builder.Default
    private String traceId = UUID.randomUUID().toString().replaceAll("-", "");
    @Builder.Default
    private Byte version = 1;
    @Builder.Default
    private ESerializeType serialize = ESerializeType.JSON;
    private EMessageType type;
    private Integer length;
}
