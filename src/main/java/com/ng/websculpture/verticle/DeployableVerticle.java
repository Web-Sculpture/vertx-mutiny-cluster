package com.ng.websculpture.verticle;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Dec, 2023
 */

@Component
@AllArgsConstructor
public class DeployableVerticle extends AbstractVerticle {

    private final static Logger LOG = LoggerFactory.getLogger(DeployableVerticle.class);

    private final NameRefVerticle nameRefVerticle;
    private final WebServerVerticle webServerVerticle;

    @Override
    public Uni<Void> asyncStart() {
        return deployVerticle();
    }

    private Uni<Void> deployVerticle() {

        Uni<String> webServer = vertx.deployVerticle(webServerVerticle);
        Uni<String> nameVerticle = vertx.deployVerticle(nameRefVerticle);

        // combine and run unis
        return Uni.combine().all().unis(webServer, nameVerticle)
                .combinedWith(list -> "successful")
                .onItem().invoke(() -> LOG.info("Deploy Verticles"))
                .onFailure().invoke((e) -> LOG.error("DeployableVerticle", e))
                .replaceWithVoid();
    }

}
