package com.midas2018mobile5.serverapp.config.security.common.repository.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by Neon K.I.D on 4/27/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RememberToken {
    private String username;

    private String series;

    private String tokenValue;

    private Date date;
}
