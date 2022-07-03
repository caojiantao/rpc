package com.caojiantao.rpc.transport.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> {

    @Builder.Default
    private Integer code = 20;
    private T body;
    private String message;
}
