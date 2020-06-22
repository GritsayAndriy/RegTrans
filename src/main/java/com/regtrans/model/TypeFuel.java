package com.regtrans.model;

import javax.persistence.*;

@Entity
@Table(name = "type_fuel")
public class TypeFuel {
    private int fuelId;
    private String name;
    private int price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "fuel_id")
    public int getFuelId() {
        return fuelId;
    }

    public void setFuelId(int fuelId) {
        this.fuelId = fuelId;
    }

    @Column (name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column (name = "price")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }
}
