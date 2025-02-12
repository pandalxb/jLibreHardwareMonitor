package io.github.pandalxb.jlibrehardwaremonitor.model;

import lombok.Data;

/**
 * Sensor model
 *
 * @author pandalxb
 */
@Data
public class Sensor {
    private String sensorType;
    private String name;
    private double value;
}
