package com.parkingservice.project.exception;

public class SlotUnavailableException extends RuntimeException {
    public SlotUnavailableException(String errorMessage) {
        super(errorMessage);
    }
}
