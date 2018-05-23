package com.midas2018mobile5.serverapp.Model.Internal.errCode;

import lombok.Data;

@Data
public class ResponseError {
    public String errMsg;
    public int errCode;

    public ResponseError(MidasStatus status) {
        this.errCode = status.code;
        this.errMsg = status.msg;
    }
}
