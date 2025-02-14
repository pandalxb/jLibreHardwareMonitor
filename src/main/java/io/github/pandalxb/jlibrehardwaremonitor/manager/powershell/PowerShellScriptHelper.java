/*
 * Copyright 2016-2018 Javier Garcia Alonso.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.pandalxb.jlibrehardwaremonitor.manager.powershell;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import io.github.pandalxb.jlibrehardwaremonitor.config.ComputerConfig;
import io.github.pandalxb.jlibrehardwaremonitor.util.SensorsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pandalxb
 */
class PowerShellScriptHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(PowerShellScriptHelper.class);

	private static final String LINE_BREAK = System.lineSeparator();

    private static LibType libType;

	static {
		intLibType();
	}

	private static void intLibType() {
		try {
			WinReg.HKEY hKey = WinReg.HKEY_LOCAL_MACHINE;
			String keyPath = "SOFTWARE\\Microsoft\\NET Framework Setup\\NDP\\v4\\Full";
			String itemName = "Release";
			boolean isItemExist = Advapi32Util.registryValueExists(hKey, keyPath, itemName);
			if(!isItemExist) {
				libType = LibType.TYPE_OPEN;
				return;
			}
			int release = Advapi32Util.registryGetIntValue(hKey, keyPath, itemName);
			if (release < 461808) {
				// lower than .NET 4.7.2
				libType = LibType.TYPE_OPEN;
				return;
			}
			String version = PowerShellOperations.GET.getEnvironmentVersion();
			String[] versionArray = version.split("\\.");
			int versionMajor = Integer.parseInt(versionArray[0]);
			int versionMinor = Integer.parseInt(versionArray[1]);
			int versionBuild = Integer.parseInt(versionArray[2]);
			int versionRevision = Integer.parseInt(versionArray[3]);
			if(versionMajor > 4 || (versionMajor == 4 && versionMinor > 0) || (versionMajor == 4 && versionMinor == 0 && versionBuild > 30319) || (versionMajor == 4 && versionMinor == 0 && versionBuild == 30319 && versionRevision >= 42000)) {
				// 4.0.30319.42000 or later
				libType = LibType.TYPE_LIBRE;
			} else {
				libType = LibType.TYPE_OPEN;
			}
		} catch (Exception e) {
			LOGGER.error("intLibType error:", e);
			libType = LibType.TYPE_OPEN;
		}
	}

	enum LibType {
		TYPE_LIBRE,
		TYPE_OPEN
	}

	// Hides constructor
	private PowerShellScriptHelper() {
	}

	private static String dllImport() {
		StringBuilder code = new StringBuilder();

		if (LibType.TYPE_LIBRE.equals(libType)) {
			code.append("[System.Reflection.Assembly]::LoadFile(\"").append(SensorsUtils.generateLibPath("/lib/", "HidSharp.dll")).append("\") | Out-Null;").append(LINE_BREAK);
			code.append("[System.Reflection.Assembly]::LoadFile(\"").append(SensorsUtils.generateLibPath("/lib/", "LibreHardwareMonitorLib.dll")).append("\") | Out-Null;").append(LINE_BREAK);
		} else {
			code.append("[System.Reflection.Assembly]::LoadFile(\"").append(SensorsUtils.generateLibPath("/lib/", "OpenHardwareMonitorLib.dll")).append("\") | Out-Null;").append(LINE_BREAK);
		}

		return code.toString();
	}

	private static String newComputerInstance(ComputerConfig config) {
		StringBuilder code = new StringBuilder();

		if (LibType.TYPE_LIBRE.equals(libType)) {
			code.append("$PC = New-Object LibreHardwareMonitor.Hardware.Computer;").append(LINE_BREAK);

			code.append(String.format("$PC.IsBatteryEnabled = $%b;", config.isBatteryEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.IsControllerEnabled = $%b;", config.isControllerEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.IsCpuEnabled = $%b;", config.isCpuEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.IsGpuEnabled = $%b;", config.isGpuEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.IsMemoryEnabled = $%b;", config.isMemoryEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.IsMotherboardEnabled = $%b;", config.isMotherboardEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.IsNetworkEnabled = $%b;", config.isNetworkEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.IsPsuEnabled = $%b;", config.isPsuEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.IsStorageEnabled = $%b;", config.isStorageEnabled())).append(LINE_BREAK);
		} else {
			code.append("$PC = New-Object OpenHardwareMonitor.Hardware.Computer;").append(LINE_BREAK);

			code.append(String.format("$PC.FanControllerEnabled = $%b;", config.isControllerEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.CPUEnabled = $%b;", config.isCpuEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.GPUEnabled = $%b;", config.isGpuEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.RAMEnabled = $%b;", config.isMemoryEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.MainboardEnabled = $%b;", config.isMotherboardEnabled())).append(LINE_BREAK);
			code.append(String.format("$PC.HDDEnabled = $%b;", config.isStorageEnabled())).append(LINE_BREAK);
		}

		return code.toString();
	}

	private static String sensorsQueryLoop() {
		StringBuilder code = new StringBuilder();

		code.append("try").append(LINE_BREAK);
		code.append("{").append(LINE_BREAK);
		code.append("$PC.Open();").append(LINE_BREAK);
		code.append("}").append(LINE_BREAK);
		code.append("catch").append(LINE_BREAK);
		code.append("{").append(LINE_BREAK);
		code.append("$PC.Open();").append(LINE_BREAK);
		code.append("};").append(LINE_BREAK);

		code.append("$jsonHardwareList = @();").append(LINE_BREAK);
		code.append("ForEach ($hw in $PC.Hardware)").append(LINE_BREAK);
		code.append("{").append(LINE_BREAK);
		code.append("$hw.Update();").append(LINE_BREAK);
		code.append("$jsonSubHardwareList = @();").append(LINE_BREAK);
		code.append("ForEach ($subhw in $hw.SubHardware)").append(LINE_BREAK);
		code.append("{").append(LINE_BREAK);
		code.append("$subhw.Update();").append(LINE_BREAK);
		code.append("$jsonSensorList = @();").append(LINE_BREAK);
		code.append("ForEach ($sensor in $subhw.Sensors)").append(LINE_BREAK);
		code.append("{").append(LINE_BREAK);
		code.append("$sensorValue = if ([string]::IsNullOrEmpty($sensor.Value)) { 0 } else { $sensor.Value };").append(LINE_BREAK);
		code.append("$jsonSensor = \"{`\"sensorType`\":`\"$($sensor.SensorType)`\",`\"name`\":`\"$($sensor.Name)`\",`\"value`\":$sensorValue}\";").append(LINE_BREAK);
		code.append("$jsonSensorList += $jsonSensor;").append(LINE_BREAK);
		code.append("};").append(LINE_BREAK);
		code.append("$jsonSubHardware = \"{`\"hardwareType`\":`\"$($subhw.HardwareType)`\",`\"name`\":`\"$($subhw.Name)`\",`\"sensors`\":[$($jsonSensorList -join ',')]}\";").append(LINE_BREAK);
		code.append("$jsonSubHardwareList += $jsonSubHardware;").append(LINE_BREAK);
		code.append("};").append(LINE_BREAK);
		code.append("$jsonSensorList = @();").append(LINE_BREAK);
		code.append("ForEach ($sensor in $hw.Sensors)").append(LINE_BREAK);
		code.append("{").append(LINE_BREAK);
		code.append("$sensorValue = if ([string]::IsNullOrEmpty($sensor.Value)) { 0 } else { $sensor.Value };").append(LINE_BREAK);
		code.append("$jsonSensor = \"{`\"sensorType`\":`\"$($sensor.SensorType)`\",`\"name`\":`\"$($sensor.Name)`\",`\"value`\":$sensorValue}\";").append(LINE_BREAK);
		code.append("$jsonSensorList += $jsonSensor;").append(LINE_BREAK);
		code.append("};").append(LINE_BREAK);
		code.append("$jsonHardware = \"{`\"hardwareType`\":`\"$($hw.HardwareType)`\",`\"name`\":`\"$($hw.Name)`\",`\"subHardware`\":[$($jsonSubHardwareList -join ',')],`\"sensors`\":[$($jsonSensorList -join ',')]}\";").append(LINE_BREAK);
		code.append("$jsonHardwareList += $jsonHardware;").append(LINE_BREAK);
		code.append("};").append(LINE_BREAK);
		code.append("$jsonOutput = \"{`\"hardware`\": [\" + ($jsonHardwareList -join ',') + \"]}\";").append(LINE_BREAK);
		code.append("Write-Host $jsonOutput;");

		return code.toString();
	}

	private static String getPowerShellScript(ComputerConfig config) {
		StringBuilder script = new StringBuilder();

		script.append(dllImport());
		script.append(newComputerInstance(config));
		script.append(sensorsQueryLoop());

		return script.toString();
	}

	static String getPowerShellScriptForSingleLine(ComputerConfig config) {
        return getPowerShellScript(config).replaceAll(LINE_BREAK, "");
	}
}
