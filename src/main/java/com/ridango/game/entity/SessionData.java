package com.ridango.game.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Setter
public class SessionData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer sessionDataId;
    private String name;
    @Builder.Default
    private Integer attemptsLeft = 5;
    private String cocktailId;
    private String currentCocktailNameFull;
    private String currentCocktailName;   
    @Type(type="org.hibernate.type.TextType")
    private String currentRecipe;
    @Builder.Default
    private String category = "";
    @Builder.Default
    private String ingredients = "";
    @Builder.Default
    private String glass = "";
    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER,  cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CocktailData> pastCocktails = new ArrayList<>();
    @Builder.Default
    private int score = 0;
    @Builder.Default
    private String pictureURL = "";

    public boolean equals(SessionData comparand) {
        return sessionDataId == comparand.getSessionDataId()
                && this.name.equals(comparand.getName())
                && this.attemptsLeft == comparand.getAttemptsLeft()
                && this.cocktailId.equals(comparand.getCocktailId());
    }

}
