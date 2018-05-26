package com.midas2018mobile5.serverapp.Model.Internal.errCode;

public enum MidasStatus {
    BAD_USERNAME(101, "BAD USERNAME"), BAD_PASSWORD(102, "BAD PASSWORD"), USERNAME_EXISTS(103, "USERNAME EXISTS"),
    LOGIN_FAILED(301, "LOGIN FAILED"),
    BAD_MENUNAME(401, "BAD MENUNAME"), BAD_PRICE(402, "BAD PRICE"), MENU_EXISTS(403, "MENU EXISTS"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");
    public int code;
    public String msg;

    MidasStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
