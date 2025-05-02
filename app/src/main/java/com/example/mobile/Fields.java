package com.example.mobile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Fields {
    @SerializedName("Surf Break")
    public List<String> SurfBreak;

    @SerializedName("Difficulty Level")
    public int DifficultyLevel;
    public String Destination;
    public String Geocode;
    public List<String> Influencers;

    @SerializedName("Magic Seaweed Link")
    public String MagicSeaweedLink;
    public List<Photo> Photos;

    @SerializedName("Peak Surf Season Begins")
    public String PeakSurfSeasonBegins;

    @SerializedName("Peak Surf Season Ends")
    public String PeakSurfSeasonEnds;
    @SerializedName("Destination State/Country")
    public String DestinationStateCountry;

    public String Address;
}

