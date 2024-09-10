package com.ridango.game.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Highscore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer scoreId;
    private String playerName;
    private Integer score;

    public boolean equals(Highscore comparand) {
        return this.scoreId == comparand.getScoreId()
        && this.playerName.equals(comparand.getPlayerName())
        && this.score == comparand.getScore();
    }
}
