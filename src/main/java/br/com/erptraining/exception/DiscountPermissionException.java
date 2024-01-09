package br.com.erptraining.exception;

import java.util.UUID;

public class DiscountPermissionException extends RuntimeException {

    public DiscountPermissionException(String msg) {
        super(msg);
    }
}

