package com.ilirus.oauth.enums;

public enum Status {
    SUCCESS(200,"OK"),
    FAIL(500,"FAIL"),
    ACCESS_DENIED(403, "权限不足")
    ;

    private int code;
    private String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
