package io.github.pandalxb.jlibrehardwaremonitor.model;

import lombok.Data;

import java.util.List;

/**
 * Hardware model
 *
 * @author pandalxb
 */
@Data
public class Hardware {
    private String hardwareType;
    private String name;
    private List<Hardware> subHardware;
    private List<Sensor> sensors;
}
