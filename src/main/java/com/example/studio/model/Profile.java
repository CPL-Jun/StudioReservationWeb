package com.example.studio.model;

public class Profile {
    private Long id;
    private String name;
    private String instrument;
    private String genre;
    private String bio;

    // コンストラクタ
    public Profile() {}

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInstrument() { return instrument; }
    public void setInstrument(String instrument) { this.instrument = instrument; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}