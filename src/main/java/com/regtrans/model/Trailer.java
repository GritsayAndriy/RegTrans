package com.regtrans.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trailer")
public class Trailer {

    private int trailerId;
    private String brand;
    private String number;

    private Set<TimeSheet> timeSheets = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "trailer_id")
    public int getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(int trailerId) {
        this.trailerId = trailerId;
    }

    @Column(name = "brand")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @OneToMany(mappedBy = "trailer")
    public Set<TimeSheet> getTimeSheets() {
        return timeSheets;
    }

    public void setTimeSheets(Set<TimeSheet> timeSheets) {
        this.timeSheets = timeSheets;
    }

    public boolean addTimeSheets(TimeSheet timeSheet){
        timeSheet.setTrailer(this);
        return getTimeSheets().add(timeSheet);
    }

    @PreRemove
    private void preRemove() {
        for (TimeSheet s : timeSheets) {
            s.setTrailer(null);
        }
    }

    @Override
    public String toString() {
        return brand + " " + number;
    }
}
