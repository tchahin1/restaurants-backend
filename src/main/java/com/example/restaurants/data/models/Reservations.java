package com.example.restaurants.data.models;

import org.springframework.cglib.core.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timeFrom;

    @Column(nullable = false)
    private LocalDateTime timeTo;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Tables table;

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalDateTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalDateTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalDateTime timeTo) {
        this.timeTo = timeTo;
    }

    public Users getUser() {
        return user;
    }

    public Tables getTable() {
        return table;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setTable(Tables table) {
        this.table = table;
    }
}
