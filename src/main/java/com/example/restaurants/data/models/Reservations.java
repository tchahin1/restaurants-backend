package com.example.restaurants.data.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date timeFrom;

    @Column(nullable = false)
    private Date timeTo;

    @ManyToOne
    private User user;

    @ManyToOne
    private Tables table;

    public long getId() {
        return id;
    }

    public Date getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Date getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Date timeTo) {
        this.timeTo = timeTo;
    }

    public User getUser() {
        return user;
    }

    public Tables getTable() {
        return table;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTable(Tables table) {
        this.table = table;
    }
}
