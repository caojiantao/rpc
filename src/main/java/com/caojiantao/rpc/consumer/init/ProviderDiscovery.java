package com.caojiantao.rpc.consumer.init;

import com.caojiantao.rpc.RpcService;
import com.caojiantao.rpc.consumer.EnableRpcConsumer;
import com.caojiantao.rpc.consumer.proxy.ProviderFactoryBean;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Slf4j
public class ProviderDiscovery implements ImportBeanDefinitionRegistrar {

    @Override
    @SneakyThrows
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attrMap = importingClassMetadata.getAnnotationAttributes(EnableRpcConsumer.class.getName());
        String[] basePackages = (String[]) attrMap.get("basePackages");
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry) {
            @Override
            @SneakyThrows
            protected boolean isCandidateComponent(AnnotatedBeanDefinition definition) {
                AnnotationMetadata metadata = definition.getMetadata();
                boolean isCandidate = metadata.isInterface() && metadata.isIndependent();
                if (!isCandidate) {
                    return false;
                }
                String clazzName = definition.getBeanClassName();
                Class<?> clazz = Class.forName(clazzName);
                ListableBeanFactory beanFactory = (ListableBeanFactory) registry;
                String[] exists = beanFactory.getBeanNamesForType(clazz);
                if (!ObjectUtils.isEmpty(exists)) {
                    return false;
                }
                // 添加动态代理类，统一处理
                definition.setBeanClassName(ProviderFactoryBean.class.getName());
                definition.getPropertyValues().add("clazz", clazz);
                definition.getPropertyValues().add("beanFactory", beanFactory);
                return true;
            }
        };
        BeanNameGenerator beanNameGenerator = (beanDefinition, beanDefinitionRegistry) -> {
            Class clazz = (Class) beanDefinition.getPropertyValues().get("clazz");
            return clazz.getName();
        };
        scanner.setBeanNameGenerator(beanNameGenerator);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RpcService.class));
        // 扫描到的合法 candidate 自动注册
        scanner.scan(basePackages);
    }
}
