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
package io.github.pandalxb.jlibrehardwaremonitor.standalone;

import io.github.pandalxb.jlibrehardwaremonitor.config.ComputerConfig;
import io.github.pandalxb.jlibrehardwaremonitor.manager.LibreHardwareManager;
import io.github.pandalxb.jlibrehardwaremonitor.model.Computer;
import io.github.pandalxb.jlibrehardwaremonitor.model.Hardware;
import io.github.pandalxb.jlibrehardwaremonitor.model.Sensor;

import java.util.Map;

/**
 * Provides an output by console
 *
 * @author pandalxb
 *
 */
public class ConsoleOutput {
	public static void showOutput(Map<String, String> config) {
		System.out.println("Scanning sensors data...");

		Computer computer = LibreHardwareManager.createInstance(ComputerConfig.getInstance().enableAll()).getComputer();
		for (final Hardware hardware : computer.getHardware()) {
			System.out.println(String.format("HardwareType:%s, Name:%s", hardware.getHardwareType(), hardware.getName()));
			for (final Hardware subHardware : hardware.getSubHardware()) {
				System.out.println(String.format("	SubHardwareType:%s, Name:%s", subHardware.getHardwareType(), subHardware.getName()));
				for (final Sensor sensor : subHardware.getSensors()) {
					System.out.println(String.format("		SensorType:%s, Name:%s, value:%s", sensor.getSensorType(), sensor.getName(), sensor.getValue()));
				}
			}
			for (final Sensor sensor : hardware.getSensors()) {
				System.out.println(String.format("	SensorType:%s, Name:%s, value:%s", sensor.getSensorType(), sensor.getName(), sensor.getValue()));
			}
		}
	}
}
