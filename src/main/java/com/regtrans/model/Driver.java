package com.regtrans.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Component
@Entity
@Table (name = "drivers")
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
        @NamedQuery(name = "Driver.getAllWithName",
        query = "select fullName from Driver d"),
        @NamedQuery(name = "Driver.getAllWithTimeSheet",
        query = "select distinct d from Driver d "
                +"left join fetch d.transport t "
                +"left join fetch d.timeSheets th")
})
public class Driver implements Serializable {

    private int driverId;
    private String fullName;
    private Transport transport;
    private Set<TimeSheet> timeSheets = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "driver_id")
    public int getDriverId() {
        return driverId;
    }

    @Column (name = "full_name")
    public String getFullName() {
        return fullName;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport")
    public Transport getTransport() {
        return this.transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }


    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)

    public Set<TimeSheet> getTimeSheets() {
        return timeSheets;
    }

    public void setTimeSheets(Set<TimeSheet> timeSheets) {
        this.timeSheets = timeSheets;
    }

    public boolean addTimeSheets(TimeSheet timeSheet){
        timeSheet.setDriver(this);
        return getTimeSheets().add(timeSheet);
    }

    public void removeTimeSheet(TimeSheet timeSheet){
        getTimeSheets().remove(timeSheet);
    }


    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", fullName='" + fullName + '\'' +
                "}\n";
    }
}
