package com.sau.empsystem.service;

import com.sau.empsystem.dto.FootballerDto;
import com.sau.empsystem.model.Footballer;
import com.sau.empsystem.repository.FootballerRepository;
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

    // İlk istek veritabanından çekilir ve Redis'e yazılır; sonraki istekler cache'den döner.
    @Cacheable("footballers")
    public List<FootballerDto> getAllFootballerDetails() {
        return footballerRepository.findAllFootballersWithDetails();
    }

    @CacheEvict(value = "footballers", allEntries = true)
    public Footballer saveFootballer(Footballer footballer) {
        return footballerRepository.save(footballer);
    }
}