package com.ng.websculpture.config;

import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Dec, 2023
 */

@Component
public class VertxSpringIocConfig implements ApplicationContextAware, VerticleFactory {

    private ApplicationContext applicationContext;

    @Override
    public String prefix() {
        return "app";
    }

    @Override
    public void createVerticle(String name, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
        String verticleName = VerticleFactory.removePrefix(name);
        promise.complete(() -> (Verticle) applicationContext.getBean(Class.forName(verticleName)));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
