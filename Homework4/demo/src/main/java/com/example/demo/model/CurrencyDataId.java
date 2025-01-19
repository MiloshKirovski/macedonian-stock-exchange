package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class CurrencyDataId implements Serializable {

    private String companyName;
    private String date;

    public CurrencyDataId() {}

    public CurrencyDataId(String companyName, String date) {
        this.companyName = companyName;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyDataId that = (CurrencyDataId) o;
        return Objects.equals(companyName, that.companyName) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, date);
    }
}
