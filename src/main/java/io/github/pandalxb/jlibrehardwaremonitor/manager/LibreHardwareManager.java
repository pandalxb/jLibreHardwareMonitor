package io.github.pandalxb.jlibrehardwaremonitor.manager;

import io.github.pandalxb.jlibrehardwaremonitor.config.ComputerConfig;
import io.github.pandalxb.jlibrehardwaremonitor.manager.powershell.PowerShellOperations;
import io.github.pandalxb.jlibrehardwaremonitor.model.Computer;
import io.github.pandalxb.jlibrehardwaremonitor.model.Hardware;
import io.github.pandalxb.jlibrehardwaremonitor.model.Sensor;
import io.github.pandalxb.jlibrehardwaremonitor.util.CollectionUtils;
import io.github.pandalxb.jlibrehardwaremonitor.util.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LibreHardwareManager
 *
 * @author pandalxb
 */
public class LibreHardwareManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LibreHardwareManager.class);
    private static final Map<String, String> hardwareTypeAliasMap;
    private static final long BUFFER_SECONDS = 1;
    private static LibreHardwareManager instance;
    private ComputerConfig config;
    private Computer computer;
    private long lastUpdateTime = -1;

    static {
        hardwareTypeAliasMap = new HashMap<>();
        hardwareTypeAliasMap.put("Memory", "RAM");
        hardwareTypeAliasMap.put("Motherboard", "Mainboard");
        hardwareTypeAliasMap.put("Storage", "HDD");
    }

    private LibreHardwareManager() {
        new LibreHardwareManager(null);
    }

    private LibreHardwareManager(ComputerConfig config) {
        if(config == null) {
            // default config
            config = ComputerConfig.getInstance().setCpuEnabled(true);
        }
        setConfig(config);
    }

    public static LibreHardwareManager getInstance(ComputerConfig config) {
        if(instance == null) {
            instance = createInstance(config);
        } else {
            instance.setConfig(config);
        }
        return instance;
    }

    public static LibreHardwareManager createInstance() {
        return createInstance(null);
    }

    public static LibreHardwareManager createInstance(ComputerConfig config) {
        return new LibreHardwareManager(config);
    }

    private void setConfig(ComputerConfig config) {
        this.config = config;
    }

    public Computer getComputer() {
        String jsonData = PowerShellOperations.GET.getComputerJsonData(config);
        Computer computer = null;
        try {
            computer = JsonParser.parseJson(jsonData);
        } catch (Exception e) {
            LOGGER.error("JsonParser.parseJson error:{}, jsonData:{}", e, jsonData);
        } finally {
            // Make sure the computer is not null
            if(computer == null) {
                computer = new Computer();
            }
        }
        return computer;
    }

    public List<Sensor> querySensors(String hardwareType, String sensorType) {
        if(lastUpdateTime < 0 || System.currentTimeMillis() - lastUpdateTime > BUFFER_SECONDS * 1000L) {
            computer = getComputer();
            lastUpdateTime = System.currentTimeMillis();
        }
        List<Sensor> list = new ArrayList<>();
        if(computer == null) {
            return list;
        }
        if(CollectionUtils.isNotEmpty(computer.getHardware())) {
            for(Hardware hardware : computer.getHardware()) {
                if (isHardwareTypeMatch(hardware.getHardwareType(), hardwareType)) {
                    if(CollectionUtils.isNotEmpty(hardware.getSensors())) {
                        for(Sensor sensor : hardware.getSensors()) {
                            if(sensor.getSensorType().equalsIgnoreCase(sensorType)) {
                                list.add(sensor);
                            }
                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(hardware.getSubHardware())) {
                    for(Hardware subHardware : hardware.getSubHardware()) {
                        if (isHardwareTypeMatch(subHardware.getHardwareType(), hardwareType)) {
                            if(CollectionUtils.isNotEmpty(subHardware.getSensors())) {
                                for(Sensor sensor : subHardware.getSensors()) {
                                    if(sensor.getSensorType().equalsIgnoreCase(sensorType)) {
                                        list.add(sensor);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private boolean isHardwareTypeMatch(String hardwareType, String queryHardwareType) {
        return hardwareType.equalsIgnoreCase(queryHardwareType)
                || (hardwareTypeAliasMap.containsKey(queryHardwareType) && hardwareType.equalsIgnoreCase(hardwareTypeAliasMap.get(queryHardwareType)))
                || (queryHardwareType.equalsIgnoreCase("gpu") && hardwareType.toLowerCase().startsWith("gpu"));
    }
}
