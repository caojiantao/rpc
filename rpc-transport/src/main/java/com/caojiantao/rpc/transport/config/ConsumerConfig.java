package com.caojiantao.rpc.transport.config;

import com.caojiantao.rpc.transport.serialize.ESerializeType;
import lombok.Data;

/**
 * @author caojiantao
 */
@Data
public class ConsumerConfig {

    private ESerializeType serialize;
}
