package com.midas2018mobile5.serverapp.Model.External.Cafe;

import javax.persistence.*;

@Entity
@Table(name = "cafeMenu")
public class Cafe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    @Column(nullable = false, length = 20)
    public String name;

    @Column(nullable = false, length = 10)
    public int price;

    @Column(nullable = false, length = 500)
    public String imgpath;
}
