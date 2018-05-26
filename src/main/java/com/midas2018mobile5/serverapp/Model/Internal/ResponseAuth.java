package com.midas2018mobile5.serverapp.Model.Internal;

import lombok.Data;

@Data
public class ResponseAuth {
    public String token;

    public ResponseAuth(String token) {
        this.token = token;
    }
}
