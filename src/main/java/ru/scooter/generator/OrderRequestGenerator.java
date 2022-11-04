package ru.scooter.generator;

import ru.scooter.dto.OrdersRequest;

import static java.lang.Integer.valueOf;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class OrderRequestGenerator {


    public static OrdersRequest randomOrderRequest(String deliveryDate, String[] color) {
        OrdersRequest ordersRequest = new OrdersRequest();
        ordersRequest.setFirstName(randomAlphabetic(10));
        ordersRequest.setLastName(randomAlphabetic(10));
        ordersRequest.setAddress(randomAlphabetic(20));
        ordersRequest.setMetroStation(valueOf(randomNumeric(1, 3)));
        ordersRequest.setPhone(randomAlphabetic(16));
        ordersRequest.setRentTime(Integer.parseInt(randomNumeric(1, 2)));
        ordersRequest.setDeliveryDate(deliveryDate);
        ordersRequest.setComment(randomAlphabetic(15));
        ordersRequest.setColor(color);
        return ordersRequest;

    }

}
