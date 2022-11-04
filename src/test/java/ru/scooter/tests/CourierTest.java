package ru.scooter.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.scooter.client.CourierClient;
import ru.scooter.generator.DeleteRequestGenerator;
import ru.scooter.generator.LoginRequestGenerator;
import ru.scooter.dto.CourierRequest;
import ru.scooter.dto.DeleteRequest;
import ru.scooter.dto.LoginRequest;
import ru.scooter.steps.CourierSteps;


import static ru.scooter.generator.CourierRequestGenerator.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CourierTest {
    private static CourierClient courierClient;
    CourierSteps courierStep = new CourierSteps();
    private Integer id;


    @BeforeEach
    public void setUp() {
        courierClient = new CourierClient();
    }


    @Test
    @Epic(value = "Couriers")
    @Feature(value = "Registration")
    @DisplayName("Creating new courier.Positive")
    public void courierShouldBeCreatedPositiveTest() {
        //create
        CourierRequest courierRequest =courierStep.createNewCourier();
        //login
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    @Epic(value = "Couriers")
    @Feature(value = "Registration")
    @DisplayName("Creating courier with same login.Negative")
    public void sameLoginRegistrationNegativeTest() {
        //create
        CourierRequest courierRequest = randomCourierRequest();
        courierClient.create(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
        //create again
        courierClient.create(courierRequest)
                .assertThat()
                .statusCode(SC_CONFLICT);
    }

    @Test
    @Epic(value = "Couriers")
    @Feature(value = "Registration")
    @DisplayName("Creating courier only with mandatory fields.Positive")
    public void courierShouldBeCreatedOnlyMandatoryFieldsPositiveTest() {
        //create
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setPassword(randomAlphanumeric(10));
        courierRequest.setLogin(randomAlphabetic(10));
        courierClient.create(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
        //login
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");

    }

    @Test
    @Epic(value = "Couriers")
    @Feature(value = "Registration")
    @DisplayName("Empty login registration.Negative")
    public void emptyLoginRegistrationNegativeTest() {
        //create
        CourierRequest courierRequest = randomCourierRequestNoLogin();
        courierClient.create(courierRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST);

    }

    @Epic(value = "Couriers")
    @Feature(value = "Registration")
    @Test
    @DisplayName("Empty password registration.Negative")
    public void emptyPasswordRegistrationNegativeTest() {
        //create
        CourierRequest courierRequest = randomCourierRequestNoPassword();
        courierClient.create(courierRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST);
    }

    @AfterEach
    public void tearDown() {
        if (id != null) {
            DeleteRequest deleteRequest = DeleteRequestGenerator.from(id);
            courierClient.delete(deleteRequest.getId())
                    .assertThat()
                    .body("ok", equalTo(true));
        }
    }
}
