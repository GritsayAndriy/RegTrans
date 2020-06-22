package com.regtrans.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;





@Component
@Entity
@Table(name = "drivers")
@NamedQueries({
        @NamedQuery(name = "Driver.findAll",
                query = "select distinct d from Driver d"),
        @NamedQuery(name = "Driver.findByIdWithTransport",
                query = "select distinct d from Driver d "
                        + "left join fetch d.transport t "
                        + "where d.id = :id"),
        @NamedQuery(name = "Driver.findById",
                query = "select distinct d from Driver d "
                        + "where d.id = :id"),
//        @NamedQuery(name = "Driver.getAllWithName",
//                query = "select fullName from Driver d"),
        @NamedQuery(name = "Driver.getWithSomeMonthTimeSheet",
                query = "select distinct d from Driver d "
                        + "left join fetch d.transport t "),
        @NamedQuery(name = "Driver.getAllWithTimeSheet",
                query = "select distinct d from Driver d "
                        + "left join fetch d.transport t "
                        + "left join fetch d.timeSheets ts ")

})
public class Driver implements Serializable {

    private int driverId;
    private String lastName;
    private String firstName;
    private String fatherName;
    private Transport transport;
    private List<TimeSheet> timeSheets = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    public int getDriverId() {
        return driverId;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "father_name")
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

//    public String getFullName() {
//        return lastName +" "+ firstName + " " + fatherName;
//    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport")
    public Transport getTransport() {
        return this.transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }


    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<TimeSheet> getTimeSheets() {
        return timeSheets;
    }

    public void setTimeSheets(List<TimeSheet> timeSheets) {
        this.timeSheets = timeSheets;
    }

    public boolean addTimeSheets(TimeSheet timeSheet) {
        timeSheet.setDriver(this);
        return getTimeSheets().add(timeSheet);
    }

    public void removeTimeSheet(TimeSheet timeSheet) {
        getTimeSheets().remove(timeSheet);
    }


    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
//                ", fullName='" + getFullName() + '\'' +
                "}\n";
    }
}
