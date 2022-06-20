package com.example.stomp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class VStompServer extends AbstractVerticle {

    @Override
    public void start() {
        log.info("Stomp Server Verticle is started");

        var stompServerOptions = new StompServerOptions()
                .setPort(-1)
                .setWebsocketBridge(true)
                .setWebsocketPath("/stomp");

        StompServer stompServer = StompServer.create(vertx, stompServerOptions)
                .handler(StompServerHandler.create(vertx));

        var httpServer = vertx.createHttpServer(
                new HttpServerOptions().setWebSocketSubProtocols(Arrays.asList("v10.stomp", "v11.stomp", "v12.stomp"))
        ).webSocketHandler(stompServer.webSocketHandler())
                .listen(9090);

    }
}
