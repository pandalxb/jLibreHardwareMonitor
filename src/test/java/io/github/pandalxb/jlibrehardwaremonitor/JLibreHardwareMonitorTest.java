package io.github.pandalxb.jlibrehardwaremonitor;

import io.github.pandalxb.jlibrehardwaremonitor.config.ComputerConfig;
import io.github.pandalxb.jlibrehardwaremonitor.manager.LibreHardwareManager;
import io.github.pandalxb.jlibrehardwaremonitor.model.Computer;
import io.github.pandalxb.jlibrehardwaremonitor.model.Sensor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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
        Assert.assertFalse(computer.getHardware().isEmpty());
    }

    @Test
    public void testGetComputerWithNoneEnabled() {
        System.out.println("testGetComputerWithNoneEnabled");
        LibreHardwareManager libreHardwareManager = LibreHardwareManager.createInstance(ComputerConfig.getInstance().setAllEnabled(false));
        Computer computer = libreHardwareManager.getComputer();
        System.out.println("computer:" + computer);
        Assert.assertTrue(computer.getHardware().isEmpty());
    }

    @Test
    public void testQuerySensors() {
        System.out.println("testQuerySensors");
        LibreHardwareManager libreHardwareManager = LibreHardwareManager.createInstance(ComputerConfig.getInstance().setCpuEnabled(true));
        List<Sensor> sensors = libreHardwareManager.querySensors("CPU", "Temperature");
        System.out.println("sensors:" + sensors);
        Assert.assertFalse(sensors.isEmpty());
    }

    @Test
    public void testQuerySensorsWithWrongConfig() {
        System.out.println("testQuerySensorsWithWrongConfig");
        LibreHardwareManager libreHardwareManager = LibreHardwareManager.createInstance(ComputerConfig.getInstance().setStorageEnabled(true));
        List<Sensor> sensors = libreHardwareManager.querySensors("CPU", "Temperature");
        System.out.println("sensors:" + sensors);
        Assert.assertTrue(sensors.isEmpty());
    }
}
