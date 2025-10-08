package com.example.studio.dto;

public class ProfileEditForm {
    private String mainInstrument;
    private String[] subInstruments;
    private String otherInstruments;
    private String favoriteBands;
    private String gender;
    private Integer age;
    private String comment;

    public String getMainInstrument() { return mainInstrument; }
    public void setMainInstrument(String mainInstrument) { this.mainInstrument = mainInstrument; }

    public String[] getSubInstruments() { return subInstruments; }
    public void setSubInstruments(String[] subInstruments) { this.subInstruments = subInstruments; }

    public String getOtherInstruments() { return otherInstruments; }
    public void setOtherInstruments(String otherInstruments) { this.otherInstruments = otherInstruments; }

    public String getFavoriteBands() { return favoriteBands; }
    public void setFavoriteBands(String favoriteBands) { this.favoriteBands = favoriteBands; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}