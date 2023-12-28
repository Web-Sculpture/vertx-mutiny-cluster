package com.ng.websculpture.verticle;

import com.ng.websculpture.config.ConfigData;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Dec, 2023
 */

@Component
public class NameRefVerticle extends AbstractVerticle {

    private final String id;

    public NameRefVerticle() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public Uni<Void> asyncStart() {
        return Uni.createFrom().item(vertx.eventBus().consumer(ConfigData.REF_ID_MSG, message -> {
            message.reply(new JsonObject()
                    .put("message", "Welcome to Vertx Mutiny Infinispan Cluster Manager")
                    .put("id", id).encode());
        })).replaceWithVoid();
    }

}
