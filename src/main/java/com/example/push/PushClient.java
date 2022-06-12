package com.example.push;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushClient extends AbstractVerticle {

    @Override
    public void start() {
        log.info("Push Client Verticle is started");
        HttpClientOptions clientOptions= new HttpClientOptions()
                .setProtocolVersion(HttpVersion.HTTP_2);

        HttpClient client = vertx.createHttpClient(clientOptions);

        client.request(HttpMethod.GET, 9090, "localhost", "/data-push").compose(request -> {


            request.pushHandler(push -> {
                log.info("Push request is sent, {}", request.absoluteURI());
                push.response().compose(HttpClientResponse::body).onSuccess(body -> {
                    log.info("Push response: {}", body);
                }).onFailure(e -> {
                    log.error("Push response error: {}", e.getMessage());
                });
            });
            return request.send().compose(response -> {
                log.info("Push response: {}", response.body());
                return response.body();
            });
        }).onSuccess(body -> {
            log.info("Get response: {}", body);
        }).onFailure(e -> {
            log.error("Push Client is failed", e);
        });
    }
}
