package com.sau.empsystem.repository;

import com.sau.empsystem.dto.EmpDto;
import com.sau.empsystem.model.Emp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpRepository extends JpaRepository<Emp, Integer> {

    // Tablo ekranı için Self-JOIN (Yönetici Adı) ve Dept JOIN (Departman Adı) içeren sorgu
    @Query("SELECT new com.sau.empsystem.dto.EmpDto(" +
            "e.empno, e.ename, e.job, m.ename, e.hiredate, e.sal, e.comm, d.dname, e.img) " +
            "FROM Emp e " +
            "LEFT JOIN Emp m ON e.mgr = m.empno " +
            "LEFT JOIN Dept d ON e.deptno = d.deptno")
    List<EmpDto> findAllEmpWithDetails();
}