package com.sau.empsystem.repository;

import com.sau.empsystem.dto.FootballerDto;
import com.sau.empsystem.model.Footballer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FootballerRepository extends JpaRepository<Footballer, Integer> {

    // Futbolcu ve takım bilgilerini tek sorguda DTO olarak getirir.
    @Query("SELECT new com.sau.empsystem.dto.FootballerDto(" +
            "f.footballerid, CONCAT(f.name, ' ', f.surname), t.teamname, t.city, " +
            "f.shirtnumber, f.marketvalue, f.photo) " +
            "FROM Footballer f " +
            "JOIN f.team t ORDER BY f.footballerid")
    List<FootballerDto> findAllFootballersWithDetails();
}