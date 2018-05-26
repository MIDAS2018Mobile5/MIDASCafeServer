package com.midas2018mobile5.serverapp.Model.External.Order;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orderList")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    @Column(nullable = false)
    public Long bid;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String menu;

    @Column(nullable = false)
    public int price;

    @Column(nullable = false)
    public Date date;

    @Column(nullable = false, columnDefinition = "boolean default false")
    public boolean isordered;
}
