package io.github.springstudent.rpc.mqttclient2.core;

import io.github.springstudent.client.core.RpcExportFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringExportFactory implements RpcExportFactory, ApplicationContextAware {

    private ApplicationContext ac;

    @Override
    public Object getExport(Class<?> clzz) {
        return ac.getBean(clzz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }
}
