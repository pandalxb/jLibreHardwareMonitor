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
import io.github.pandalxb.jlibrehardwaremonitor.manager.powershell.PowerShellOperations;
import io.github.pandalxb.jlibrehardwaremonitor.model.Computer;
import io.github.pandalxb.jlibrehardwaremonitor.model.Hardware;
import io.github.pandalxb.jlibrehardwaremonitor.model.Sensor;
import io.github.pandalxb.jlibrehardwaremonitor.util.OSDetector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * Provides an output using a Swing based GUI
 *
 * @author pandalxb
 *
 */
public class GuiOutput {

	public static void showOutput(final Map<String, String> config) {
		EventQueue.invokeLater(() -> {
            JLibreHardwareMonitorGUI gui = new GuiOutput().new JLibreHardwareMonitorGUI(config);
            gui.setVisible(true);
        });
	}

	@SuppressWarnings("serial")
	class JLibreHardwareMonitorGUI extends JFrame {
		private Map<String, String> config;
		private JTable table = new JTable();

		public JLibreHardwareMonitorGUI(Map<String, String> config) {
			this.config = config;
			initUI();
		}

		private void initUI() {
			setTitle("JLibreHardwareMonitor");
			setSize(600, 800);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);

			if (OSDetector.isWindows() && !PowerShellOperations.isAdministrator()) {
				JOptionPane.showMessageDialog(this,
						"You have not executed JLibreHardwareMonitor in Administrator mode, so CPU temperature sensors will not be detected.");
			}

			new GuiUpdater(this).execute();

		}

		private DefaultTableModel initModel() {
            return new DefaultTableModel(new Object[] { "Hardware/Sensor", "Value" }, 0);
		}

		private DefaultTableModel calculateModel() {
			DefaultTableModel model = initModel();

			Computer computer = LibreHardwareManager.createInstance(ComputerConfig.getInstance().enableAll()).getComputer();
			for (final Hardware hardware : computer.getHardware()) {
				model.addRow(new String[] { String.format("%s:%s", hardware.getHardwareType(), hardware.getName()), "" });
				for (final Hardware subHardware : hardware.getSubHardware()) {
					model.addRow(new String[] { String.format("<html><div style='padding-left: 20px;'>%s:%s</div></html>", subHardware.getHardwareType(), subHardware.getName()), "" });
					for (final Sensor sensor : subHardware.getSensors()) {
						model.addRow(new String[] { String.format("<html><div style='padding-left: 40px;'>%s:%s</div></html>", sensor.getSensorType(), sensor.getName()), String.valueOf(sensor.getValue()) });
					}
				}
				for (final Sensor sensor : hardware.getSensors()) {
					model.addRow(new String[] { String.format("<html><div style='padding-left: 20px;'>%s:%s</div></html>", sensor.getSensorType(), sensor.getName()), String.valueOf(sensor.getValue()) });
				}
			}

			return model;
		}

		private class GuiUpdater extends SwingWorker<Void, Void> {
			public GuiUpdater(JLibreHardwareMonitorGUI jLibreHardwareMonitorGUI) {
				JScrollPane scrollPane = new JScrollPane(table);
				table.setFillsViewportHeight(true);
				jLibreHardwareMonitorGUI.add(scrollPane);

			}

			@Override
			protected Void doInBackground() {
				while (true) {
					table.setModel(calculateModel());
					table.getColumnModel().getColumn(0).setPreferredWidth(400);
					table.getColumnModel().getColumn(1).setPreferredWidth(200);
					//Thread.sleep(2000);
				}
			}

		}
	}

}
