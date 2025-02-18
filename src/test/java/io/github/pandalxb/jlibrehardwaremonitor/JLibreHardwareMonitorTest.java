package io.github.pandalxb.jlibrehardwaremonitor;

import io.github.pandalxb.jlibrehardwaremonitor.config.ComputerConfig;
import io.github.pandalxb.jlibrehardwaremonitor.manager.LibreHardwareManager;
import io.github.pandalxb.jlibrehardwaremonitor.model.Computer;
import io.github.pandalxb.jlibrehardwaremonitor.model.Sensor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * author: luxb
 * create: 2025/2/12 9:32
 **/
public class JLibreHardwareMonitorTest {
    @Test
    public void testGetComputerWithAllEnabled() {
        System.out.println("testGetComputerWithAllEnabled");
        LibreHardwareManager libreHardwareManager = LibreHardwareManager.createInstance(ComputerConfig.getInstance().enableAll());
        Computer computer = libreHardwareManager.getComputer();
        System.out.println("computer:" + computer);
        assertThat("Hardware should be not null", computer.getHardware().isEmpty(), is(false));
    }

    @Test
    public void testGetComputerWithNoneEnabled() {
        System.out.println("testGetComputerWithNoneEnabled");
        LibreHardwareManager libreHardwareManager = LibreHardwareManager.createInstance(ComputerConfig.getInstance().setAllEnabled(false));
        Computer computer = libreHardwareManager.getComputer();
        System.out.println("computer:" + computer);
        assertThat("Hardware should be null", computer.getHardware().isEmpty(), is(true));
    }

    @Test
    public void testQuerySensors() {
        System.out.println("testQuerySensors");
        LibreHardwareManager libreHardwareManager = LibreHardwareManager.createInstance(ComputerConfig.getInstance().setCpuEnabled(true));
        List<Sensor> sensors = libreHardwareManager.querySensors("CPU", "Temperature");
        System.out.println("sensors:" + sensors);
        assertThat("Sensors should be not null", sensors.isEmpty(), is(false));
    }

    @Test
    public void testQuerySensorsWithWrongConfig() {
        System.out.println("testQuerySensorsWithWrongConfig");
        LibreHardwareManager libreHardwareManager = LibreHardwareManager.createInstance(ComputerConfig.getInstance().setStorageEnabled(true));
        List<Sensor> sensors = libreHardwareManager.querySensors("CPU", "Temperature");
        System.out.println("sensors:" + sensors);
        assertThat("Sensors should be null", sensors.isEmpty(), is(true));
    }
}
