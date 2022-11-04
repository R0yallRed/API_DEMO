package ru.scooter.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.scooter.client.CourierClient;
import ru.scooter.client.OrdersClient;
import ru.scooter.dto.DeleteRequest;
import ru.scooter.dto.OrdersRequest;
import ru.scooter.generator.DeleteRequestGenerator;
import ru.scooter.steps.CourierSteps;
import ru.scooter.steps.OrderSteps;

import static java.lang.Integer.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static ru.scooter.generator.OrderRequestGenerator.*;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class OrderTest {
    private static final OrdersClient ordersClient = new OrdersClient();
    private static final CourierClient courierClient = new CourierClient();
    private  static final OrderSteps orderSteps = new OrderSteps();
    private  static final CourierSteps courierSteps = new CourierSteps();
    private static Integer courierId;

    @BeforeAll
    public static void createCourier() {
        courierId = courierSteps.getCourierId(courierSteps.createNewCourier());
    }

    @ParameterizedTest
    @DisplayName("Creating new order with different colors. Parametrized")
    @Epic(value = "Orders")
    @Feature(value = "Create Order")
    @MethodSource("ru.scooter.tests.OrderTest#getOrderData")
    public void createNewOrderDifferentColorsTest(String deliveryDate, String[] color) {
        OrdersRequest ordersRequest = randomOrderRequest(deliveryDate, color);
        ordersClient.createOrder(ordersRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .body("track", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Accept order positive test")
    @Epic(value = "Orders")
    @Feature(value = "Accept Order")
    public void acceptOrderPositiveTest() {
        ordersClient.acceptOrder(orderSteps.getOrderId(orderSteps.createNewOrder()), courierId)
                .assertThat()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @RepeatedTest(5)
    @DisplayName("Accept order with wrong id negative test")
    @Epic(value = "Orders")
    @Feature(value = "Accept Order")
    public void acceptOrderWrongIdNegativeTest() {
        ordersClient.acceptOrder(parseInt(randomNumeric(6, 9)), courierId)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Accept order with wrong courier login negative test")
    @Epic(value = "Orders")
    @Feature(value = "Accept Order")
    public void acceptOrderWrongCourierLoginNegativeTest() {
        ordersClient.acceptOrder(orderSteps.getOrderId(orderSteps.createNewOrder()), parseInt(randomNumeric(7, 9)))
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Accept already accepted order negative test")
    @Epic(value = "Orders")
    @Feature(value = "Accept Order")
    public void acceptAlreadyAcceptedOrderNegativeTest() {
        int orderId = orderSteps.getOrderId(orderSteps.createNewOrder());
        ordersClient.acceptOrder(orderId, courierId)
                .assertThat()
                .statusCode(SC_OK);
        ordersClient.acceptOrder(orderId, courierId)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот заказ уже в работе"));

    }
    @Test
    @DisplayName("Finish order positive test")
    @Epic(value = "Orders")
    @Feature(value = "Finish Order")
    public void finishOrderPositiveTest() {
        int orderId = orderSteps.getOrderId(orderSteps.createNewOrder());
        ordersClient.acceptOrder(orderId, courierId)
                .assertThat()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));

        ordersClient.finishOrder(orderId)
                .assertThat()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Finish order nonexistent ID negative test")
    @Epic(value = "Orders")
    @Feature(value = "Finish Order")
    public void finishOrderNonExistentIdNegativeTest() {
        ordersClient.finishOrder(valueOf(randomNumeric(7,9)))
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    /*@Test
    @Disabled
    @DisplayName("Finish order without  ID negative test")
    @Epic(value = "Orders")
    @Feature(value = "Finish Order")
    public void finishOrderWithoutIdNegativeTest() {
        ordersClient.finishOrderEmpty()
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }*/


    @Test
    @DisplayName("Try to finish unfinishible test negative test")
    @Epic(value = "Orders")
    @Feature(value = "Finish Order")
    public void finishUnfinishibleOrderNegativeTest() {
        int orderId = orderSteps.getOrderId(orderSteps.createNewOrder());
        ordersClient.finishOrder(orderId)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот заказ нельзя завершить"));
    }


    @AfterAll
    static void tearDown() {
        if (courierId != null) {
            DeleteRequest deleteRequest = DeleteRequestGenerator.from(courierId);
            courierClient.delete(deleteRequest.getId())
                    .assertThat()
                    .body("ok", equalTo(true));
        }
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
}
