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
@Table(name = "emp")
public class Emp implements Serializable {

    @Id
    @Column(name = "empno")
    private Integer empno;

    @Column(name = "ename")
    private String ename;

    @Column(name = "job")
    private String job;

    @Column(name = "mgr")
    private Integer mgr;

    @Column(name = "hiredate")
    private String hiredate;

    @Column(name = "sal")
    private Integer sal;

    @Column(name = "comm")
    private Integer comm;

    @Column(name = "deptno")
    private Integer deptno;

    @Column(name = "img")
    private String img;
}