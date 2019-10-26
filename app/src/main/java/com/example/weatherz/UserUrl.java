package com.example.weatherz;

public class UserUrl {
    private String location;
    private String weather_type;

    UserUrl(){}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeather_type() {
        return weather_type;
    }

    public void setWeather_type(String weather_type) {
        this.weather_type = weather_type;
    }
}
