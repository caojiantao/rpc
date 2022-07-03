package com.caojiantao.rpc.transport.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest {

    private Class<?> clazz;
    private Class<?> returnType;
    private String name;
    private Object[] args;
    private Class<?>[] parameterTypes;
}
