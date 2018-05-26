package com.midas2018mobile5.serverapp.Model.Internal;

import lombok.Data;

@Data
public class ResponseAuth {
    public String token;
    public String role;

    public ResponseAuth(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
