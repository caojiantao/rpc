package com.caojiantao.rpc.provider;

import com.caojiantao.rpc.provider.init.ProviderRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author caojiantao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ProviderRegister.class)
public @interface EnableRpcProvider {

}
