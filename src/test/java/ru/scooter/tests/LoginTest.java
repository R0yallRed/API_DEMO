package ru.scooter.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.scooter.client.CourierClient;
import ru.scooter.dto.CourierRequest;
import ru.scooter.dto.DeleteRequest;
import ru.scooter.dto.LoginRequest;
import ru.scooter.generator.DeleteRequestGenerator;
import ru.scooter.generator.LoginRequestGenerator;
import static ru.scooter.generator.CourierRequestGenerator.randomCourierRequest;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTest {
    static CourierClient courierClient;
    private static Integer id;
    private static final CourierRequest courierRequest = randomCourierRequest();

    @BeforeAll
    public static void setUp() {
        courierClient = new CourierClient();
        courierClient.create(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

    }

    @Test
    @Epic(value = "Couriers")
    @Feature(value = "Login")
    @DisplayName("Courier can login.Positive")
    public void courierCanLoginPositiveTest() {
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
    @DisplayName("Wrong login.Negative")
    @Epic(value = "Couriers")
    @Feature(value = "Login")
    public void wrongLoginNegativeTest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(randomAlphabetic(10));
        loginRequest.setPassword(courierRequest.getPassword());
        courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND);

    }

    @Test
    @DisplayName("Empty login.Negative")
    @Epic(value = "Couriers")
    @Feature(value = "Login")
    public void emptyLoginNegativeTest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("");
        loginRequest.setPassword(courierRequest.getPassword());
        courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Wrong password.Negative")
    @Epic(value = "Couriers")
    @Feature(value = "Login")
    public void wrongPasswordNegativeTest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(courierRequest.getLogin());
        loginRequest.setPassword(randomAlphanumeric(10));
        courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND);

    }

    @Test
    @DisplayName("Empty password.Negative")
    @Epic(value = "Couriers")
    @Feature(value = "Login")
    public void emptyPasswordNegativeTest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(courierRequest.getLogin());
        loginRequest.setPassword("");
        courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Non existent courier login.Negative")
    @Epic(value = "Couriers")
    @Feature(value = "Login")
    public void nonExistentCourierLoginNegativeTest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(randomAlphabetic(10));
        loginRequest.setPassword(randomAlphanumeric(10));
        courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND);
    }

    @AfterAll
    static void tearDown() {
        if (id != null) {
            DeleteRequest deleteRequest = DeleteRequestGenerator.from(id);
            courierClient.delete(deleteRequest.getId())
                    .assertThat()
                    .body("ok", equalTo(true));
        }
    }
}