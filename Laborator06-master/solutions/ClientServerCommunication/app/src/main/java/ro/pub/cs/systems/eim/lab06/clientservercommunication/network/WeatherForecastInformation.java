package ro.pub.cs.systems.eim.lab06.clientservercommunication.network;

/**
 * Created by ionut_000 on 05/16/2018.
 */

public class WeatherForecastInformation
{
    private double temperature;
    private double windSpeed;
    private String condition;
    private int pressure;
    private int humidity;

    public WeatherForecastInformation(double temperature, double windSpeed, String condition, int pressure, int humidity) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.condition = condition;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "WeatherForecastInformation{" +
                "temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                ", condition='" + condition + '\'' +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }
}
