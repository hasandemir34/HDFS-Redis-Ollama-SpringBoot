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
@Table(name = "dept") //mysqldeki dept adlı tablo ile eşleniyor.
public class Dept implements Serializable {  //cache için serializable yazıyoruz

    @Id
    @Column(name = "deptno")
    private Integer deptno;

    @Column(name = "dname")
    private String dname;

    @Column(name = "loc")  //departman konumu location
    private String loc;
}