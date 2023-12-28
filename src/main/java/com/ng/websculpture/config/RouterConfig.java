package com.ng.websculpture.config;

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.ext.cluster.infinispan.ClusterHealthCheck;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Dec, 2023
 */

@Configuration
public class RouterConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouterConfig.class);

    @Bean
    public Router router() {

        Vertx vertx = VertxHolderConfig.getInstance().getVertx();
        Router router = Router.router(vertx);

        Handler<Promise<Status>> procedure = ClusterHealthCheck.createProcedure(vertx.getDelegate(), true);
        HealthChecks checks = HealthChecks.create(vertx.getDelegate()).register("cluster-health", procedure);
        router.getDelegate().get("/readiness").handler(HealthCheckHandler.createWithHealthChecks(checks));

        try {
            router.route().handler(ctx -> {
                ctx.response()
                        // do not allow proxies to cache the data
                        .putHeader("Cache-Control", "no-store, no-cache")
                        // prevents Internet Explorer from MIME - sniffing a
                        // response away from the declared content-type
                        .putHeader("X-Content-Type-Options", "nosniff")
                        // Strict HTTPS (for about ~6Months)
                        .putHeader("Strict-Transport-Security", "max-age=" + 15768)
                        // IE8+ do not allow opening of attachments in the context of this resource
                        .putHeader("X-Download-Options", "noopen")
                        // enable XSS for IE
                        .putHeader("X-XSS-Protection", "1; mode=block")
                        // deny frames
                        .putHeader("X-FRAME-OPTIONS", "DENY");
                ctx.next();
            });
        } catch (Exception e) {
            LOGGER.error("RouterConfig", e);
        }

        return router;
    }

}
