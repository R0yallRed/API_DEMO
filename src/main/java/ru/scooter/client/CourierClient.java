package ru.scooter.client;

import io.restassured.response.ValidatableResponse;
import ru.scooter.dto.CourierRequest;
import ru.scooter.dto.LoginRequest;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {
    private static final String COURIER = "courier";
    private static final String COURIER_LOGIN = "courier/login";

    //CREATE
    public ValidatableResponse create(CourierRequest courierRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(courierRequest)
                .post(COURIER)
                .then();
    }

    //LOGIN
    public ValidatableResponse login(LoginRequest loginRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(loginRequest)
                .post(COURIER_LOGIN)
                .then();
    }

    //DELETE
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getDefaultRequestSpec())
                .body("")
                .delete("courier/" + id)
                .then();

    }

    //GET ORDERS COUNT
    public ValidatableResponse ordersCount (int id){
        return given()
                .spec(getDefaultRequestSpec())
                .body("")
                .get("courier/" + id +"ordersCount")
                .then();
    }
}
