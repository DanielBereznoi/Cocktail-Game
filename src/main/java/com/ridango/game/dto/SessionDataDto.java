package com.ridango.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
@Setter
public class SessionDataDto {

    private Integer sessionDataId;
    private String name;
    @Builder.Default
    private Integer attemptsLeft = 5;
    private String currentCocktailName;
    private String currentRecipe;
    @Builder.Default
    private String category = "";
    @Builder.Default
    private String ingredients = "";
    @Builder.Default
    private String pictureURL = "";
    @Builder.Default
    private String glass = "";
    @Builder.Default
    private int score = 0;

    public boolean equals(SessionDataDto comparand) {
        return this.name.equals(comparand.name)
                && this.attemptsLeft == comparand.getAttemptsLeft()
                && this.currentCocktailName.equals(comparand.getCurrentCocktailName())
                && this.currentRecipe.equals(comparand.getCurrentRecipe())
                && this.score == comparand.getScore()
                && this.sessionDataId == comparand.getSessionDataId()
                && this.category.equals(comparand.getCategory())
                && this.ingredients.equals(comparand.getIngredients())
                && this.pictureURL.equals(comparand.getPictureURL())
                && this.glass.equals(comparand.getGlass());
    }

}
