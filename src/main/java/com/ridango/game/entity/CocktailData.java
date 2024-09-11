package com.ridango.game.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class CocktailData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer entityID;
    private String name;
    private String cocktailId;
    private String ingredients;
    @Type(type="org.hibernate.type.TextType")
    private String recipe;
    private String category;
    private String pictureURL;
    private String glass;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SessionData session;

    public boolean equals(CocktailData comparand) {
        return this.entityID == comparand.getEntityID()
                && this.name.equals(comparand.getName())
                && this.cocktailId.equals(comparand.getCocktailId())
                && this.ingredients.equals(comparand.ingredients)
                && this.recipe.equals(comparand.getRecipe())
                && this.category.equals(comparand.getCategory())
                && this.pictureURL.equals(comparand.getPictureURL())
                && this.glass.equals(comparand.getGlass());
    }
}
