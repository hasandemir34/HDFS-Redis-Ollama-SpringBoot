package com.sau.empsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dept")
public class Dept implements Serializable {

    @Id
    @Column(name = "deptno")
    private Integer deptno;

    @Column(name = "dname")
    private String dname;

    @Column(name = "loc")
    private String loc;
}