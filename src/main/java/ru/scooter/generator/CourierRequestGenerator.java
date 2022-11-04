package ru.scooter.generator;

import com.github.javafaker.Faker;
import ru.scooter.dto.CourierRequest;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class CourierRequestGenerator {
    private static final Faker faker = Faker.instance();
    public static CourierRequest randomCourierRequest() {
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setFirstName(faker.name().firstName());
        courierRequest.setPassword(randomAlphanumeric(10));
        courierRequest.setLogin(randomAlphabetic(10));
        return courierRequest;
    }

    public static CourierRequest randomCourierRequestNoPassword() {
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setFirstName(faker.name().firstName());
        courierRequest.setLogin(randomAlphabetic(10));
        return courierRequest;
    }

    public static CourierRequest randomCourierRequestNoLogin() {
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setFirstName(faker.name().firstName());
        courierRequest.setPassword(randomAlphanumeric(10));
        return courierRequest;
    }
}
