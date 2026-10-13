package com.sau.empsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpDto implements Serializable {

    private Integer empno;
    private String ename;
    private String job;
    private String mgrName;    // Self-JOIN ile gelen yönetici adı
    private String hiredate;
    private Integer sal;
    private Integer comm;
    private String deptName;   // Dept JOIN ile gelen departman adı
    private String img;  //hdfs resim yolu
}