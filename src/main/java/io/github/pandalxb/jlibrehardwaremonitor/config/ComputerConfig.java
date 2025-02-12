package io.github.pandalxb.jlibrehardwaremonitor.config;

/**
 * ComputerConfig
 *
 * @author pandalxb
 */
public class ComputerConfig {
    private boolean isBatteryEnabled = false;
    private boolean isControllerEnabled = false;
    private boolean isCpuEnabled = false;
    private boolean isGpuEnabled = false;
    private boolean isMemoryEnabled = false;
    private boolean isMotherboardEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean isPsuEnabled = false;
    private boolean isStorageEnabled = false;
    private ComputerConfig() {

    }
    public static ComputerConfig getInstance() {
        return new ComputerConfig();
    }

    public ComputerConfig enableAll() {
        setAllEnabled(true);
        return this;
    }

    public ComputerConfig setAllEnabled(boolean isEnabled) {
        this.isBatteryEnabled = isEnabled;
        this.isControllerEnabled = isEnabled;
        this.isCpuEnabled = isEnabled;
        this.isGpuEnabled = isEnabled;
        this.isMemoryEnabled = isEnabled;
        this.isMotherboardEnabled = isEnabled;
        this.isNetworkEnabled = isEnabled;
        this.isPsuEnabled = isEnabled;
        this.isStorageEnabled = isEnabled;
        return this;
   }

    public ComputerConfig setBatteryEnabled(boolean isBatteryEnabled) {
        this.isBatteryEnabled = isBatteryEnabled;
        return this;
    }

    public ComputerConfig setControllerEnabled(boolean isControllerEnabled) {
        this.isControllerEnabled = isControllerEnabled;
        return this;
    }

    public ComputerConfig setCpuEnabled(boolean isCpuEnabled) {
        this.isCpuEnabled = isCpuEnabled;
        return this;
    }

    public ComputerConfig setGpuEnabled(boolean isGpuEnabled) {
        this.isGpuEnabled = isGpuEnabled;
        return this;
    }

    public ComputerConfig setMemoryEnabled(boolean isMemoryEnabled) {
        this.isMemoryEnabled = isMemoryEnabled;
        return this;
    }

    public ComputerConfig setMotherboardEnabled(boolean isMotherboardEnabled) {
        this.isMotherboardEnabled = isMotherboardEnabled;
        return this;
    }

    public ComputerConfig setNetworkEnabled(boolean isNetworkEnabled) {
        this.isNetworkEnabled = isNetworkEnabled;
        return this;
    }

    public ComputerConfig setPsuEnabled(boolean isPsuEnabled) {
        this.isPsuEnabled = isPsuEnabled;
        return this;
    }

    public ComputerConfig setStorageEnabled(boolean isStorageEnabled) {
        this.isStorageEnabled = isStorageEnabled;
        return this;
    }

    public boolean isBatteryEnabled() {
        return isBatteryEnabled;
    }

    public boolean isControllerEnabled() {
        return isControllerEnabled;
    }

    public boolean isCpuEnabled() {
        return isCpuEnabled;
    }

    public boolean isGpuEnabled() {
        return isGpuEnabled;
    }

    public boolean isMemoryEnabled() {
        return isMemoryEnabled;
    }

    public boolean isMotherboardEnabled() {
        return isMotherboardEnabled;
    }

    public boolean isNetworkEnabled() {
        return isNetworkEnabled;
    }

    public boolean isPsuEnabled() {
        return isPsuEnabled;
    }

    public boolean isStorageEnabled() {
        return isStorageEnabled;
    }
}
