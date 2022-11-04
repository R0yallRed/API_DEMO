package ru.scooter.generator;

import ru.scooter.dto.CourierRequest;
import ru.scooter.dto.LoginRequest;

public class LoginRequestGenerator {
    public static LoginRequest from(CourierRequest courierRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(courierRequest.getLogin());
        loginRequest.setPassword(courierRequest.getPassword());
        return loginRequest;

    }
}
