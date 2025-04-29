package com.example.mobile;

public class Spot {
    protected CharSequence name;
    protected CharSequence location;
    protected int image;

    public Spot(CharSequence name, CharSequence location, int image)
    {
        this.name = name;
        this.image = image;
        this.location = location;
    }

    public CharSequence getName()
    {
        return this.name;
    }
    public CharSequence getLocation()
    {
        return this.location;
    }
    public int getImage()
    {
        return this.image;
    }
}
