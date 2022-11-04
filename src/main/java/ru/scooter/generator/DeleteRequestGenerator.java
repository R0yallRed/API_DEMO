package ru.scooter.tests.generator;

import ru.scooter.tests.dto.DeleteRequest;

public class DeleteRequestGenerator {
    public static DeleteRequest from(int id) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setId(id);
        return deleteRequest;

    }
}
