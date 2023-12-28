package com.ng.websculpture.controller;

import com.ng.websculpture.config.ConfigData;
import com.ng.websculpture.config.VertxHolderConfig;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.function.Function;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Dec, 2023
 */

@Controller
public class HomeController implements Function<RoutingContext, Uni<Void>> {

    private final Vertx vertx;

    @Autowired
    public HomeController(Router router) {
        this.vertx = VertxHolderConfig.getInstance().getVertx();
        router.get("/").respond(this);
    }

    @Override
    public Uni<Void> apply(RoutingContext rc) {
        return vertx.eventBus().request(ConfigData.REF_ID_MSG, "")
                .flatMap(message -> {
                    String data = message.body().toString();
                    return rc.response().setStatusCode(200)
                            .putHeader(ConfigData.CT, ConfigData.CV)
                            .send(new JsonObject(data).encode());
                });
    }

}
