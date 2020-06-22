package com.regtrans.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "transports")
public class Transport {

    //Transport table fields
    private int transportId;
    private String brand;
    private String model;
    private String number;
    private Integer typeTransport;
    private TypeFuel typeFuel;
    private Double staticUseFuel;

    private Set<Driver> drivers = new HashSet<>();
    private Set<TimeSheet> timeSheets = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "transport_id")
    public int getTransportId() {
        return transportId;
    }

    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }



    @Column (name = "brand")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }




    @Column (name = "model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }



    @Column (name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }




    @Column (name = "type_transport")
    public Integer getTypeTransport() {
        return typeTransport;
    }

    public void setTypeTransport(Integer typeTransport) {
        this.typeTransport = typeTransport;
    }



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_fuel")
    public TypeFuel getTypeFuel() {
        return typeFuel;
    }

    public void setTypeFuel(TypeFuel typeFuel) {
        this.typeFuel = typeFuel;
    }




    @Column (name = "static_use_fuel")
    public Double getStaticUseFuel() {
        return staticUseFuel;
    }

    public void setStaticUseFuel(Double staticUseFuel) {
        this.staticUseFuel = staticUseFuel;
    }


    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL, orphanRemoval = true)

    public Set<Driver> getDrivers() {
        return drivers;
    }


    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    public boolean addDriver(Driver driver){
        driver.setTransport(this);
        return getDrivers().add(driver);
    }

    public void removeDriver(Driver driver){
        getDrivers().remove(driver);
    }

/*
    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<TimeSheet> getTimeSheets() {
        return timeSheets;
    }

    public void setTimeSheets(Set<TimeSheet> timeSheets) {
        this.timeSheets = timeSheets;
    }

    public boolean addTimeSheets(TimeSheet timeSheet){
        timeSheet.setTransport(this);
        return getTimeSheets().add(timeSheet);
    }

    public void removeTimeSheet(TimeSheet timeSheet){
        getTimeSheets().remove(timeSheet);
    }


 */
    @Override
    public String toString() {
        return getBrand()+" "+ getModel();
    }
}
