package br.esc.software.domain.apis;

public class Forecast {

    private String date;
    private String weekday;
    private float max;
    private float min;
    private String description;
    private String condition;

    // Getter Methods

    public String getDate() {
        return date;
    }

    public String getWeekday() {
        return weekday;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }

    // Setter Methods

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
