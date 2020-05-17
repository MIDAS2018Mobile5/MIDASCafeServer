package com.midas2018mobile5.serverapp.domain.cafe;

import com.midas2018mobile5.serverapp.domain.order.Mcorder;
import com.midas2018mobile5.serverapp.dto.cafe.CafeDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "What is coffee ?")
    @Column(nullable = false, length = 20, unique = true)
    private String name;

    @Column(name = "description", length = 100)
    private String desc;

    @NotNull(message = "How much is coffee ?")
    @Column(nullable = false, length = 10)
    private int price;

    @Embedded
    private CafeImage image;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Mcorder> orders = new HashSet<>();

    @Builder
    public Cafe(String name, String desc, int price, MultipartFile file) throws IOException {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.image = CafeImage.builder().file(file).build();
    }

    public void update(CafeDto.Req dto, MultipartFile file) throws IOException {
        this.name = dto.getName();
        this.price = dto.getPrice();

        if (file != null) {
            this.image.deleteImage();
            this.image.saveImage(file);
        }
    }
}
