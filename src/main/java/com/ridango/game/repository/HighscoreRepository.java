package com.ridango.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ridango.game.entity.Highscore;

@Repository
public interface HighscoreRepository extends JpaRepository<Highscore, Integer> {
    
}
