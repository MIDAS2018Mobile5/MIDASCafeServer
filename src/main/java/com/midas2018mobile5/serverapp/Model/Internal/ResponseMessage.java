package com.midas2018mobile5.serverapp.Model.Internal;

import lombok.Data;

@Data
public class ResponseMessage {
    public boolean success;

    public ResponseMessage(boolean success) {
        this.success = success;
    }
}
