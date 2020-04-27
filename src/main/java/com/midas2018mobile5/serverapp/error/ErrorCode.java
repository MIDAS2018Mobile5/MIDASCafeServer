package com.midas2018mobile5.serverapp.error;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    UNAUTHORIZED(401, "C007", "Unauthorized Request"),

    // User
    USER_NOT_FOUND(400, "U001", "User not found"),
    USER_DUPLICATION(400, "U002", "User ID is exists"),
    LOGIN_INPUT_INVALID(400, "U003", "Login is invalid"),

    // Cafe
    MENU_NOT_FOUND(400, "M001", "Menu not found"),
    MENU_DUPLICATION(400, "M002", "Menu is exists"),

    // Order
    BAD_ORDER_CHANGE(400, "O001", "Order status change failed"),
    ORDER_NOT_FOUND(400, "O002", "Order not found");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
