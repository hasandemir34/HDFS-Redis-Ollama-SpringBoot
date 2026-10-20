package com.sau.empsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "footballers")
public class Footballer implements Serializable {

    @Id
    @Column(name = "footballerid")
    private Integer footballerid;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teamid", nullable = false)
    private Team team;

    @Column(name = "shirtnumber")
    private Integer shirtnumber;

    @Column(name = "marketvalue")
    private Long marketvalue;

    @Column(name = "photo")
    private String photo;
}
