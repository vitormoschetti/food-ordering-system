package com.food.ordering.system.order.service.domain.exception;

public abstract class DomainException extends RuntimeException {

    public DomainException(final String message) {
        super(message);
    }

    public DomainException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
