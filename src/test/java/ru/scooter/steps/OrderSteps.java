package ru.scooter.steps;

import io.qameta.allure.Step;
import org.hamcrest.Matchers;
import ru.scooter.client.OrdersClient;
import ru.scooter.dto.OrdersRequest;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static ru.scooter.generator.OrderRequestGenerator.randomOrderRequest;
import static org.hamcrest.Matchers.equalTo;

public class OrderSteps {
    OrdersClient ordersClient = new OrdersClient();

    @Step("Create New Order")
    public int createNewOrder() {
        OrdersRequest ordersRequest = randomOrderRequest("2023-10-30", new String[]{"BLACK"});
        return ordersClient.createOrder(ordersRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .body("track", Matchers.notNullValue())
                .extract()
                .path("track");

    }

    @Step("Get order ID")
    public int getOrderId(int orderTrack) {
        return ordersClient.getOrderId(orderTrack)
                .assertThat()
                .statusCode(SC_OK)
                .body("order.id", Matchers.notNullValue())
                .extract()
                .path("order.id");
    }

    @Step("Finish order")
    public void finishOrder(int orderTrack) {
        ordersClient.finishOrder(orderTrack)
                .assertThat()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));

    }
}
