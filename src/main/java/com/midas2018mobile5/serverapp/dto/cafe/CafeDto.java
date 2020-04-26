package com.midas2018mobile5.serverapp.dto.cafe;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.domain.cafe.CafeImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class CafeDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @Valid
        private String name;

        @Valid
        private String desc;

        @Valid
        private int price;

        @Builder
        public Req(String name, String desc, int price) {
            this.name = name;
            this.desc = desc;
            this.price = price;
        }

        public Cafe toEntity(MultipartFile file) throws IOException {
            return Cafe.builder()
                    .name(this.name)
                    .desc(desc)
                    .price(this.price)
                    .file(file)
                    .build();
        }
    }

    @Getter
    public static class Res {
        private String name;
        private int price;
        private CafeImage imgPath;

        public Res(Cafe cafe) {
            this.name = cafe.getName();
            this.price = cafe.getPrice();
            this.imgPath = cafe.getImage();
        }
    }
}
