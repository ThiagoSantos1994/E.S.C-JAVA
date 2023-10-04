package br.esc.software.domain.apis;

import java.util.ArrayList;

public class Results {
    private float temp;
    private String date;
    private String time;
    private String condition_code;
    private String description;
    private String currently;
    private String cid;
    private String city;
    private String img_id;
    private float humidity;
    private String wind_speedy;
    private String sunrise;
    private String sunset;
    private String condition_slug;
    private String city_name;
    private ArrayList<Forecast> forecast;

    // Getter Methods

    public float getTemp() {
        return temp;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCondition_code() {
        return condition_code;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrently() {
        return currently;
    }

    public String getCid() {
        return cid;
    }

    public String getCity() {
        return city;
    }

    public String getImg_id() {
        return img_id;
    }

    public float getHumidity() {
        return humidity;
    }

    public String getWind_speedy() {
        return wind_speedy;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getCondition_slug() {
        return condition_slug;
    }

    public String getCity_name() {
        return city_name;
    }

    // Setter Methods

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCondition_code(String condition_code) {
        this.condition_code = condition_code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrently(String currently) {
        this.currently = currently;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setWind_speedy(String wind_speedy) {
        this.wind_speedy = wind_speedy;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public void setCondition_slug(String condition_slug) {
        this.condition_slug = condition_slug;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public ArrayList<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(ArrayList<Forecast> forecast) {
        this.forecast = forecast;
    }

}
