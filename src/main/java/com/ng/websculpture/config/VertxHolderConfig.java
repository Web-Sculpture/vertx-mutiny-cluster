package com.ng.websculpture.config;

import io.vertx.mutiny.core.Vertx;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Dec, 2023
 */

public class VertxHolderConfig {

    private Vertx vertx;

    private VertxHolderConfig() {
    }

    public static final VertxHolderConfig getInstance() {
        return VertxHolderConfigInit.Instance;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    private static final class VertxHolderConfigInit {
        private static final VertxHolderConfig Instance = new VertxHolderConfig();
    }
}
