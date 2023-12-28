package com.ng.websculpture.verticle;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.tracing.TracingPolicy;
import io.vertx.mutiny.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Dec, 2023
 */

@Component
public class WebServerVerticle extends AbstractVerticle {

    private final static Logger LOG = LoggerFactory.getLogger(WebServerVerticle.class);

    private final Router router;

    private String host = "127.0.0.1";
    private int port = 0;

    @Autowired
    public WebServerVerticle(Router router) {
        this.router = router;
    }

    @Override
    public Uni<Void> asyncStart() {
        return createServer();
    }

    private Uni<Void> createServer() {
        HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setHost(host).setPort(port)
                .setTracingPolicy(TracingPolicy.ALWAYS);

        return vertx.createHttpServer(httpServerOptions)
                .requestHandler(router)
                .listen(port, host)
                .onItem()
                .invoke(httpServer -> LOG.info("Http Server is Running on Port: {}", httpServer.actualPort()))
                .replaceWithVoid();
    }

}
