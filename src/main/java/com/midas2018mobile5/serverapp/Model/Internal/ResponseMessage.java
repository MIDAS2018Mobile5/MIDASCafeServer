package com.midas2018mobile5.serverapp.Model.Internal;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ResponseMessage {
    @Nullable public String message;
    public MidasStatus status;

    public ResponseMessage(MidasStatus sc, String msg) {
        this.status = sc;
        this.message = msg;
    }
}
