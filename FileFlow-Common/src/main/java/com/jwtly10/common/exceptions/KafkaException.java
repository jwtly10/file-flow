package com.jwtly10.common.exceptions;

public class KafkaException extends RuntimeException {
    public KafkaException(String message) {
        super(message);
    }
}
