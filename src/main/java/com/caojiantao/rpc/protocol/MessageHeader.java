package com.caojiantao.rpc.protocol;

import com.caojiantao.rpc.serialize.ESerializeType;
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
    private String traceId;
    @Builder.Default
    private Byte version = 1;
    @Builder.Default
    private ESerializeType serialize = ESerializeType.JSON;
    private EMessageType type;
    private Integer length;
}
