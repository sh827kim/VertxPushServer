package com.example.push;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j
public class PushServer extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        createPushServer();
        deployVerticle();
    }

    private void createPushServer() {

        HttpServer pushServer = vertx.createHttpServer(new HttpServerOptions());

        pushServer.requestHandler(request -> {
            String path = request.path();
            HttpServerResponse response = request.response();

            if ("/data-push".equals(path)) {
                log.info("data-push");

                response.push(HttpMethod.GET, "/data-push", pushRes -> {
                    if (pushRes.succeeded()) {
                        HttpServerResponse pushResponse = pushRes.result();
                        var json = new JsonObject();
                        json.put("time", LocalDateTime.now().toString());
                        log.info(json.encode());
                        pushResponse.send(json.encode());
                    } else {

                    }
                });
            }
        });
        pushServer.listen(9090)
                .onSuccess(v -> log.info("Push Server is started"))
                .onFailure(e -> log.error("Push Server is failed", e));

    }
    private void deployVerticle() {
        vertx.setTimer(5000, id -> {
            vertx.deployVerticle(new PushClient());
        });
    }
}
