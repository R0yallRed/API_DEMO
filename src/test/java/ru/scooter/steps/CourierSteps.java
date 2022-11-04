package ru.scooter.steps;

import io.qameta.allure.Step;
import ru.scooter.client.CourierClient;
import ru.scooter.dto.CourierRequest;
import ru.scooter.dto.LoginRequest;
import ru.scooter.generator.LoginRequestGenerator;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.scooter.generator.CourierRequestGenerator.randomCourierRequest;

public class CourierSteps {
    static CourierClient courierClient = new CourierClient();

    @Step("CreateNewCourier")
    public  CourierRequest createNewCourier() {
        CourierRequest courierRequest = randomCourierRequest();
        courierClient.create(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
        return courierRequest;
    }

    @Step("new Courier Login")
    public  int getCourierId(CourierRequest courierRequest) {
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        return courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
    }
}
