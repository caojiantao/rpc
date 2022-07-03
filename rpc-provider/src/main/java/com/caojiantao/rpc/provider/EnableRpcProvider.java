package com.caojiantao.rpc.provider;

import com.caojiantao.rpc.provider.init.ProviderRegister;
import com.caojiantao.rpc.provider.io.Server;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author caojiantao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ProviderRegister.class, Server.class})
public @interface EnableRpcProvider {

}
