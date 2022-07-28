package com.caojiantao.rpc.protocol;

import lombok.Data;

/**
 * | 1魔数 | 32追踪 | 1版本 | 1序列化 |
 * ----------------------------------
 * |   1报文类型    |   4报文长度     |
 * ----------------------------------
 * |             内容                |
 *
 * @author caojiantao
 */
@Data
public class Message<T> {

    private MessageHeader header;
    private T body;
}
