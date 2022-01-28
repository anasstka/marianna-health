package com.example.mhapp;

import java.io.Serializable;

public class UserRationData implements Serializable {
    private float breakfastCalories;
    private float lunchCalories;
    private float dinnerCalories;
    private float snackCalories;

    private float proteins;
    private float fats;
    private float carbohydrates;

    public UserRationData() {
        breakfastCalories = 0;
        lunchCalories = 0;
        dinnerCalories = 0;
        snackCalories = 0;

        proteins = 0;
        fats = 0;
        carbohydrates = 0;
    }

    public float getBreakfastCalories() {
        return breakfastCalories;
    }

    public void setBreakfastCalories(float breakfastCalories) {
        this.breakfastCalories = breakfastCalories;
    }

    public float getLunchCalories() {
        return lunchCalories;
    }

    public void setLunchCalories(float lunchCalories) {
        this.lunchCalories = lunchCalories;
    }

    public float getDinnerCalories() {
        return dinnerCalories;
    }

    public void setDinnerCalories(float dinnerCalories) {
        this.dinnerCalories = dinnerCalories;
    }

    public float getSnackCalories() {
        return snackCalories;
    }

    public void setSnackCalories(float snackCalories) {
        this.snackCalories = snackCalories;
    }

    public float getProteins() {
        return proteins;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public float getFats() {
        return fats;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
}
