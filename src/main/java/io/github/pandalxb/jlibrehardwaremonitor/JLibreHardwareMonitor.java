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
package io.github.pandalxb.jlibrehardwaremonitor;

import io.github.pandalxb.jlibrehardwaremonitor.manager.powershell.PowerShellOperations;
import io.github.pandalxb.jlibrehardwaremonitor.standalone.ConsoleOutput;
import io.github.pandalxb.jlibrehardwaremonitor.standalone.GuiOutput;
import io.github.pandalxb.jlibrehardwaremonitor.util.OSDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class of JLibreHardwareMonitor. <br>
 * As library, it provides access to sensors information using a fluent api.
 * <p>
 * If launched as standalone application it prints in console all the retrieved
 * sensor information.
 *
 * @author pandalxb
 */
public enum JLibreHardwareMonitor {

	get;

	private static final Logger LOGGER = LoggerFactory.getLogger(JLibreHardwareMonitor.class);

	static {
		checkRights();
	}

	private static void checkRights() {
		if (OSDetector.isWindows() && !PowerShellOperations.isAdministrator()) {
			LOGGER.warn("You have not executed jLibreHardwareMonitor in Administrator mode, so CPU temperature sensors will not be detected.");
		}
	}

	JLibreHardwareMonitor() {

	}

	/**
	 * Standalone entry point
	 *
	 * @param args program arguments
	 */
	public static void main(String[] args) {
		boolean guiMode = false;
		Map<String, String> overriddenConfig = new HashMap<>();
		for (final String arg : args) {
			if ("--debug".equals(arg)) {
				overriddenConfig.put("debugMode", "true");
			}
			if ("--gui".equals(arg)) {
				guiMode = true;
			}
		}

		if (guiMode) {
			GuiOutput.showOutput(overriddenConfig);
		} else {
			//ConsoleOutput.showOutput(overriddenConfig);
			GuiOutput.showOutput(overriddenConfig);//test
		}
	}
}
