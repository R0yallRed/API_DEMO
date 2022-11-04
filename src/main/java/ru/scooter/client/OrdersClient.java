package ru.scooter.client;

import io.restassured.response.ValidatableResponse;
import ru.scooter.dto.OrdersRequest;

import static io.restassured.RestAssured.given;

public class OrdersClient extends RestClient{
    public static final String ORDERS = "orders";
    public static final String ORDERS_LIMIT_10_PAGE_0 = "orders?limit=10&page=0";
    public static final String ACCEPT = "orders/accept/";
    public static final String TRACK = "orders/track?t=";
    public static final String FINISH = "/orders/finish/";




    public ValidatableResponse createOrder(OrdersRequest ordersRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(ordersRequest)
                .post(ORDERS)
                .then();
    }

    public ValidatableResponse getOrdersCount() {
        return given()
                .spec(getDefaultRequestSpec())
                .body("")
                .get(ORDERS_LIMIT_10_PAGE_0)
                .then();
    }

    public ValidatableResponse acceptOrder( Integer orderId, Integer courierId) {
        return given()
                .spec(getDefaultRequestSpec())
                .body("")
                .put(ACCEPT + orderId + "?courierId=" + courierId)
                .then();
    }

    public ValidatableResponse getOrderId(int orderTrack) {
        return given()
                .spec(getDefaultRequestSpec())
                .body("")
                .get(TRACK  + orderTrack)
                .then();

    }
    public ValidatableResponse finishOrder(Integer orderId) {
        return given()
                .spec(getDefaultRequestSpec())
                .body("")
                .put(FINISH  + orderId)
                .then();
}

    public ValidatableResponse finishOrderEmpty() {
        return given()
                .spec(getDefaultRequestSpec())
                .body("{\n" +
                        "\"id\":\n" +
                                "}")
                .put(FINISH)
                .then();
    }
}