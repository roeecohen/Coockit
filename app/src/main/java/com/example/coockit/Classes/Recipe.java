package com.example.coockit.Classes;
import java.util.UUID;

/**
 * every recipe that user is uploading and every recipe from the internet will be saved here.
 * the recipes from users are saved in DB
 */
public class Recipe {

    private String id;
    private String name;
    private String picUrl;
    private String directions;
    private String ingredients;
    private String difficulty;
    private String preparationTime;
    private String user;

    public Recipe(){}

    public Recipe(String name, String picUrl, String directions, String ingredients, String difficulty, String preparationTime, String user) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.picUrl = picUrl;
        this.directions = directions;
        this.ingredients = ingredients;
        this.difficulty = difficulty;
        this.preparationTime = preparationTime;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
