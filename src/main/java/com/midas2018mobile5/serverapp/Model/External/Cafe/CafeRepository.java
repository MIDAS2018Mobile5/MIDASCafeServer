package com.midas2018mobile5.serverapp.Model.External.Cafe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeRepository extends CrudRepository<Cafe, Long> {
    @Query(value = "select c from Cafe c where c.name = :name")
    Cafe selectMenu(@Param(value = "name") String coffee);
    
    @Query(value = "select count(c) from Cafe c where c.name = :name")
    int existsByMenu(@Param(value = "name") String coffee);
}
