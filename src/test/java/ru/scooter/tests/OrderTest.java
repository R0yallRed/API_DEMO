package ru.scooter;

import io.qameta.allure.Step;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.scooter.client.CourierClient;
import ru.scooter.client.OrdersClient;
import ru.scooter.dto.CourierRequest;
import ru.scooter.dto.OrdersRequest;
import ru.scooter.generator.OrderRequestGenerator;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.scooter.LoginTest.courierClient;
import static ru.scooter.generator.CourierRequestGenerator.randomCourierRequest;

public class OrderTest {
   private static final OrdersClient ordersClient = new OrdersClient();
    private static final CourierClient courierClient = new CourierClient();
    private static Integer orderId;
    private static Integer courierId;



    @ParameterizedTest
    @MethodSource("ru.scooter.OrderTest#getOrderData")
    public  void createNewOrderDifferentColorsTest(String deliveryDate, String[] color){
        OrdersRequest ordersRequest = OrderRequestGenerator.randomOrderRequest(deliveryDate, color);
        ordersClient.createOrder(ordersRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .body("track", Matchers.notNullValue());

    }

    @Test
    public void acceptOrderPositiveTest() {
        createOrder();
        createNewCourier();
    }

    @Step
    public void createOrder() {
        OrdersRequest ordersRequest = OrderRequestGenerator.randomOrderRequest("2023-10-30", new String[]{"BLACK"});
        orderId = ordersClient.createOrder(ordersRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .body("track", Matchers.notNullValue())
                .extract()
                .path("track");
    }

    private static Object[][] getOrderData() {
        String[] twoColors = {"BLACK", "GRAY"};
        String[] black = {"BLACK"};
        String[] grey = {"GREY"};
        String[] empty = {};

        return new Object[][]{

                {"2023-10-30", black},
                {"2022-10-30", grey},
                {"2022-10-30", twoColors},
                {"2022-12-31", empty},
        };
    }
    @Step("CreateNewCourier")
    public CourierRequest createNewCourier() {
        CourierRequest courierRequest = randomCourierRequest();
        courierClient.create(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
        return courierRequest;
    }
}
