package io.github.pandalxb.jlibrehardwaremonitor.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Hardware model
 *
 * @author pandalxb
 */
public class Hardware {
    private String hardwareType;
    private String name;
    private List<Hardware> subHardware;
    private List<Sensor> sensors;

    public String getHardwareType() {
        return hardwareType;
    }

    public void setHardwareType(String hardwareType) {
        this.hardwareType = hardwareType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Hardware> getSubHardware() {
        return subHardware;
    }

    public void setSubHardware(List<Hardware> subHardware) {
        this.subHardware = subHardware;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        return "Hardware{" +
                "hardwareType='" + hardwareType + '\'' +
                ", name='" + name + '\'' +
                ", subHardware=" + (subHardware != null ? subHardware.stream().map(Hardware::toString).collect(Collectors.toList()) : "null") +
                ", sensors=" + (sensors != null ? sensors.stream().map(Sensor::toString).collect(Collectors.toList()) : "null") +
                '}';
    }
}
