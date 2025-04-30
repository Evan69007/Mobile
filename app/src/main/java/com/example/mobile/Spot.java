package com.example.mobile;

public class Spot {
    protected String name;
    protected String location;
    protected String image;

    public Spot(String name, String location, String image)
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
    public String getImage()
    {
        return this.image;
    }
}
