package com.example.mobile;

public class Spot {

    protected String id;

    protected String name;
    protected String location;
    protected String image;

    protected int difficulty;

    protected String peak_end;

    protected String peak_start;

    public Spot(String name, String location, String image, int difficulty, String peak_end, String peak_start, String id)
    {
        this.name = name;
        this.image = image;
        this.location = location;
        this.difficulty = difficulty;
        this.peak_end = peak_end;
        this.peak_start = peak_start;
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }
    public String getLocation()
    {
        return this.location;
    }
    public String getImage()
    {
        return this.image;
    }

    public int getDifficulty() { return this.difficulty; }
    public String getPeakEnd() { return this.peak_end; }

    public String getPeakStart() { return this.peak_start; }
}
