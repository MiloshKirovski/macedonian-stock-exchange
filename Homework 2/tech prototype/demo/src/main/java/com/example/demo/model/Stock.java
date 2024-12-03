package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
class CurrencyDataId implements Serializable {
    private String companyName;
    private String date;
}

@Data
@Entity
@Table(name = "currency_data")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CurrencyDataId.class)
public class Stock {

    @Id
    @Column(name = "companyname")
    private String companyName;

    @Id
    @Column(name = "date")
    private String date;

    @Column(name = "priceoflasttransaction")
    private String priceOfLastTransaction;

    @Column(name = "max")
    private String max;

    @Column(name = "min")
    private String min;

    @Column(name = "avgprice")
    private String avgPrice;

    @Column(name = "percentchange")
    private String percentChange;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "turnoverbest")
    private String turnoverBest;

    @Column(name = "totalturnover")
    private String totalTurnover;
}