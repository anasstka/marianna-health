package com.example.mhapp;

import java.io.Serializable;

public class UserProfileData implements Serializable {
    private UserPurpose purpose;

    private int avatarId;
    private String name;

    private float tell;
    private String dateOfBorn;
    private float startWeight;
    private float finishWeight;
    private float activityValue;
    private UserSex sex;

    private float needCalories;
    private float needWaterCount;

    private String timeOfBreakfast;
    private String timeOfLunch;
    private String timeOfDinner;
    private String timeOfSnack;
    private String timeOfWater;
    private boolean infoAboutWater;
    private boolean infoAboutFood;

    public UserProfileData() {
        purpose = UserPurpose.SLIM_PURPOSE;

        avatarId = 0;
        name = "user";

        tell = 0;
        dateOfBorn = "01.01.1950";
        startWeight = 0;
        finishWeight = 0;
        activityValue = 1.2f;

        sex = UserSex.MEN;

        needCalories = 100.0f;
        needWaterCount = 0.25f;

        timeOfBreakfast = "07:00";
        timeOfLunch = "12:00";
        timeOfDinner = "18:00";
        timeOfSnack = "15:00";
        timeOfWater = "1";

        infoAboutWater = true;
        infoAboutFood = true;
    }

    public UserProfileData(UserPurpose purpose,
                           int avatarId,
                           String name,
                           float tell,
                           String dateOfBorn,
                           float startWeight,
                           float finishWeight,
                           float activityValue,
                           UserSex sex,
                           float needCalories,
                           float needWaterCount,
                           String timeOfBreakfast,
                           String timeOfLunch,
                           String timeOfDinner,
                           String timeOfSnack,
                           String timeOfWater,
                           boolean infoAboutWater,
                           boolean infoAboutFood) {
        this.purpose = purpose;
        this.avatarId = avatarId;
        this.name = name;
        this.tell = tell;
        this.dateOfBorn = dateOfBorn;
        this.startWeight = startWeight;
        this.finishWeight = finishWeight;
        this.activityValue = activityValue;
        this.sex = sex;
        this.needCalories = needCalories;
        this.needWaterCount = needWaterCount;
        this.timeOfBreakfast = timeOfBreakfast;
        this.timeOfLunch = timeOfLunch;
        this.timeOfDinner = timeOfDinner;
        this.timeOfSnack = timeOfSnack;
        this.timeOfWater = timeOfWater;
        this.infoAboutWater = infoAboutWater;
        this.infoAboutFood = infoAboutFood;
    }

    public int getAvatarId() {
        return avatarId;
    }
    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public UserPurpose getPurpose() {
        return purpose;
    }
    public void setPurpose(UserPurpose purpose) {
        this.purpose = purpose;
    }

    public float getTell() {
        return tell;
    }
    public void setTell(float tell) {
        this.tell = tell;
    }

    public String getDateOfBorn() {
        return dateOfBorn;
    }
    public void setDateOfBorn(String dateOfBorn) {
        this.dateOfBorn = dateOfBorn;
    }

    public float getStartWeight() {
        return startWeight;
    }
    public void setStartWeight(float startWeight) {
        this.startWeight = startWeight;
    }

    public float getFinishWeight() {
        return finishWeight;
    }
    public void setFinishWeight(float finishWeight) {
        this.finishWeight = finishWeight;
    }

    public float getActivityValue() {
        return activityValue;
    }
    public void setActivityValue(float activityValue) {
        this.activityValue = activityValue;
    }

    public UserSex getSex() {
        return sex;
    }
    public void setSex(UserSex sex) {
        this.sex = sex;
    }

    public float getNeedCalories() {
        return needCalories;
    }
    public void setNeedCalories(float needCalories) {
        this.needCalories = needCalories;
    }

    public float getNeedWaterCount() {
        return needWaterCount;
    }
    public void setNeedWaterCount(float needWaterCount) {
        this.needWaterCount = needWaterCount;
    }

    public String getTimeOfBreakfast() {
        return timeOfBreakfast;
    }
    public void setTimeOfBreakfast(String timeOfBreakfast) {
        this.timeOfBreakfast = timeOfBreakfast;
    }

    public String getTimeOfLunch() {
        return timeOfLunch;
    }
    public void setTimeOfLunch(String timeOfLunch) {
        this.timeOfLunch = timeOfLunch;
    }

    public String getTimeOfDinner() {
        return timeOfDinner;
    }
    public void setTimeOfDinner(String timeOfDinner) {
        this.timeOfDinner = timeOfDinner;
    }

    public String getTimeOfSnack() {
        return timeOfSnack;
    }
    public void setTimeOfSnack(String timeOfSnack) {
        this.timeOfSnack = timeOfSnack;
    }

    public boolean isInfoAboutWater() {
        return infoAboutWater;
    }
    public void setInfoAboutWater(boolean infoAboutWater) {
        this.infoAboutWater = infoAboutWater;
    }

    public boolean isInfoAboutFood() {
        return infoAboutFood;
    }
    public void setInfoAboutFood(boolean infoAboutFood) {
        this.infoAboutFood = infoAboutFood;
    }

    public String getTimeOfWater() {
        return timeOfWater;
    }
    public void setTimeOfWater(String timeOfWater) {
        this.timeOfWater = timeOfWater;
    }
}
