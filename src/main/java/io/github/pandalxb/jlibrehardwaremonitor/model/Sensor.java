package io.github.pandalxb.jlibrehardwaremonitor.model;

/**
 * Sensor model
 *
 * @author pandalxb
 */
public class Sensor {
    private String sensorType;
    private String name;
    private double value;

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorType='" + sensorType + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
