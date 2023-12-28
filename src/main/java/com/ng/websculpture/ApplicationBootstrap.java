package com.ng.websculpture;

import com.ng.websculpture.config.ApplicationConfig;
import com.ng.websculpture.config.VertxHolderConfig;
import com.ng.websculpture.verticle.DeployableVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.cluster.infinispan.InfinispanClusterManager;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Dec, 2023
 */

public class ApplicationBootstrap {

    private final static Logger LOG = LoggerFactory.getLogger(ApplicationBootstrap.class);

    public static void main(String[] args) {

        ClusterManager clusterManager = new InfinispanClusterManager();
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setClusterManager(clusterManager);
        Vertx.clusteredVertx(vertxOptions)
                .flatMap(vertx -> {

                    VertxHolderConfig.getInstance().setVertx(vertx);
                    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
                    DeployableVerticle deployableVerticle = applicationContext.getBean(DeployableVerticle.class);

                    return vertx.deployVerticle(deployableVerticle);
                })
                .subscribe()
                .with(id -> LOG.info("Vertx Instance Id: {}", id),
                        throwable -> LOG.error("ApplicationBootstrap", throwable));
    }

}
