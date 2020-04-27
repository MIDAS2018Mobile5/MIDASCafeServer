package com.midas2018mobile5.serverapp.repository.cafe;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class CafeRepositoryImpl extends QuerydslRepositorySupport implements CafeSupportRepository {
    public CafeRepositoryImpl() {
        super(Cafe.class);
    }
}
