package com.sau.empsystem.service;

import com.sau.empsystem.dto.EmpDto;
import com.sau.empsystem.model.Emp;
import com.sau.empsystem.repository.EmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpService {

    @Autowired
    private EmpRepository empRepository;

    // Redis Önbellekleme (Cache): İlk istek veritabanından çekilir ve Redis'e yazılır.
    // İkinci istekte veri doğrudan Redis'ten döndürülür.
    @Cacheable(value = "employees")
    public List<EmpDto> getAllEmpDetails() {
        System.out.println("--> [MySQL SORGUSU] Veriler veritabanından çekiliyor...");
        return empRepository.findAllEmpWithDetails();
    }

    // Yeni çalışan eklendiğinde veya güncellendiğinde Redis cache'ini temizler
    @CacheEvict(value = "employees", allEntries = true)
    public Emp saveEmp(Emp emp) {
        return empRepository.save(emp);
    }
}