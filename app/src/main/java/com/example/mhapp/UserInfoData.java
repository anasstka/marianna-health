package com.example.mhapp;

public class UserInfoData {

    private String date;
    private float proteins;
    private float fats;

    private float carbohydrates;
    private float waterCount;
    private float breakfastCalories;
    private float lunchCalories;
    private float dinnerCalories;
    private float snackCalories;

    public UserInfoData() {}

    public UserInfoData(String date) {
        this.date = date;
        this.proteins = 0.0f;
        this.fats = 0.0f;
        this.carbohydrates = 0.0f;
        this.waterCount = 0.0f;
        this.breakfastCalories = 0.0f;
        this.lunchCalories = 0.0f;
        this.dinnerCalories = 0.0f;
        this.snackCalories = 0.0f;
    }

    public UserInfoData(String date,
                        float proteins,
                        float fats,
                        float carbohydrates,
                        float waterCount,
                        float breakfastCalories,
                        float lunchCalories,
                        float dinnerCalories,
                        float snackCalories) {
        this.date = date;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.waterCount = waterCount;
        this.breakfastCalories = breakfastCalories;
        this.lunchCalories = lunchCalories;
        this.dinnerCalories = dinnerCalories;
        this.snackCalories = snackCalories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public float getWaterCount() {
        return waterCount;
    }

    public void setWaterCount(float waterCount) {
        this.waterCount = waterCount;
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
}
