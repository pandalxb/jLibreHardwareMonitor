package io.github.pandalxb.jlibrehardwaremonitor.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Computer model
 *
 * @author pandalxb
 */
public class Computer {
    private List<Hardware> hardware;

    public List<Hardware> getHardware() {
        return hardware;
    }

    public void setHardware(List<Hardware> hardware) {
        this.hardware = hardware;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "hardware=" + (hardware != null ? hardware.stream().map(Hardware::toString).collect(Collectors.toList()) : "null") +
                '}';
    }
}
