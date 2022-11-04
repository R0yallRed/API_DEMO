package ru.scooter.tests.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.scooter.tests.config.Config;

public class RestClient {
    public RequestSpecification getDefaultRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(Config.getBaseUri())
                .setContentType(ContentType.JSON)
                .build()
                .given().log().all();
    }
}
