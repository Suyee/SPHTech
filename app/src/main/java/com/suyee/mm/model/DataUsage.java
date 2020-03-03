package com.suyee.mm.model;

public class DataUsage {
    private String year;
    private double volume;
    private String downMessage;

    public DataUsage(String year, double volume){
        this.year = year;
        this.volume = volume;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getDownMessage() {
        return downMessage;
    }

    public void setDownMessage(String downMessage) {
        this.downMessage = downMessage;
    }

    @Override
    public String toString() {
        return "DataUsage{" +
                "year='" + year + '\'' +
                ", volume=" + volume +
                ", downMessage='" + downMessage + '\'' +
                '}';
    }
}
