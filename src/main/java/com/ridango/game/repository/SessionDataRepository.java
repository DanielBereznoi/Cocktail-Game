package com.ridango.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ridango.game.entity.SessionData;

@Repository
public interface SessionDataRepository extends JpaRepository<SessionData, Integer> {

    @SuppressWarnings("unchecked")
    public SessionData save(SessionData sessionData);

    public SessionData findBySessionDataId(Integer sessionDataId);

}
