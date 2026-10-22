package com.sau.empsystem.service;

import com.sau.empsystem.dto.FootballerDto;
import com.sau.empsystem.model.Footballer;
import com.sau.empsystem.repository.FootballerRepository;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FootballerService {

    private final FootballerRepository footballerRepository;

    public FootballerService(FootballerRepository footballerRepository) {
        this.footballerRepository = footballerRepository;
    }


    @Cacheable(value = "footballer_list") //veritabanından, mysqlden alıyorum tüm oyunculaır
    public List<FootballerDto> getAllFootballerDetails() {
        return footballerRepository.findAllFootballersWithDetails();
    }

    @Cacheable(value = "footballers", key = "#id", unless = "#result == null")
    public Footballer findFootballer(Integer id) {
        return footballerRepository.findById(id).orElse(null);
    }

    @Caching(evict = {
            @CacheEvict(value = "footballers", allEntries = true),
            @CacheEvict(value = "footballer_list", allEntries = true)
    })
    public Footballer saveFootballer(Footballer footballer) {
        return footballerRepository.save(footballer);
    }

    @Caching(evict = {
            @CacheEvict(value = "footballers", key = "#id"),
            @CacheEvict(value = "footballer_list", allEntries = true)
    })
    public void deleteFootballer(Integer id) {
        footballerRepository.deleteById(id);
    }
}