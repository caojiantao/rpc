package com.caojiantao.rpc.protocol;

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
    private String name;
    private Object[] args;
    private Class<?>[] parameterTypes;
}
