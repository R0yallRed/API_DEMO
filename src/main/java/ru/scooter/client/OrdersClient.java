package ru.scooter.tests.client;

import io.restassured.response.ValidatableResponse;
import ru.scooter.tests.dto.OrdersRequest;

import static io.restassured.RestAssured.given;

public class OrdersClient extends RestClient{
    public static final String ORDERS = "orders";
    public static final String ORDERS_LIMIT_10_PAGE_0 = "orders?limit=10&page=0";
    public static final String ACCEPT = "/orders/accept/";


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

    public ValidatableResponse acceptOrder(int courierId, int orderId) {
        return given()
                .spec(getDefaultRequestSpec())
                .queryParam(courierId + "?courierId=" + orderId)
                .body("")
                .get(ACCEPT)
                .then();
    }
}
