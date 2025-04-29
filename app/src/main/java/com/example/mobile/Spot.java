package com.example.mobile;

public class Spot {
    protected String name;
    protected String location;
    protected int image;

    public Spot(String name, String location, int image)
    {
        this.name = name;
        this.image = image;
        this.location = location;
    }

    public String getName()
    {
        return this.name;
    }
    public String getLocation()
    {
        return this.location;
    }
    public int getImage()
    {
        return this.image;
    }
}
