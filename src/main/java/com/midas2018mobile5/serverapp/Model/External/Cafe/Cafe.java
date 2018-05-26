package com.midas2018mobile5.serverapp.Model.External.Cafe;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cafeMenu")
public class Cafe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    @Column(nullable = false, length = 20)
    public String name;

    @Column(nullable = false)
    public String menu;

    @Column(nullable = false, length = 10)
    public int price;
}
