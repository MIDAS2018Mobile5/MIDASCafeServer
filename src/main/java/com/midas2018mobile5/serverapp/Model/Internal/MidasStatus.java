package com.midas2018mobile5.serverapp.Model.Internal;

public enum MidasStatus {
    SUCCESS("0x00200"), FAIL("0x00400"), ERROR("0x00500");
    public String code;
    MidasStatus(String code) { this.code = code; }
}
