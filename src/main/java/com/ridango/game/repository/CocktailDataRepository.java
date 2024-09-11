package com.ridango.game.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ridango.game.entity.CocktailData;

@Repository
public interface CocktailDataRepository extends JpaRepository<CocktailData, Integer>{

    public List<CocktailData> findByCocktailId(String cocktailId) ;
}
