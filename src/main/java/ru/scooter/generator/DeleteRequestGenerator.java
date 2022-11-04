package ru.scooter.generator;

import ru.scooter.dto.DeleteRequest;

public class DeleteRequestGenerator {
    public static DeleteRequest from(int id) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setId(id);
        return deleteRequest;

    }
}
