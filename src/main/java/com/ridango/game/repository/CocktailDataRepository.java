package com.ridango.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ridango.game.entity.CocktailData;

@Repository
public interface CocktailDataRepository extends JpaRepository<CocktailData, Integer>{

    public CocktailData findByCocktailId(String cocktailId) ;
}
