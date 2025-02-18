package io.github.pandalxb.jlibrehardwaremonitor.util;

import io.github.pandalxb.jlibrehardwaremonitor.model.Computer;
import io.github.pandalxb.jlibrehardwaremonitor.model.Hardware;
import io.github.pandalxb.jlibrehardwaremonitor.model.Sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * JsonParser
 *
 * @author pandalxb
 */
public class JsonParser {

    public static Computer parseJson(String jsonString) {
        jsonString = jsonString.trim();
        if (!jsonString.startsWith("{") || !jsonString.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
        jsonString = jsonString.substring(1, jsonString.length() - 1);
        Computer computer = new Computer();
        while (!jsonString.isEmpty()) {
            int colonIndex = jsonString.indexOf(":");
            String key = jsonString.substring(0, colonIndex).trim().replaceAll("\"", "");
            jsonString = jsonString.substring(colonIndex + 1).trim();
            if (jsonString.startsWith("[")) {
                int endIndex = findMatchingBracket(jsonString, '[', ']');
                String jsonArrayStr = jsonString.substring(0, endIndex + 1);
                if ("hardware".equals(key)) {
                    List<Hardware> hardwareList = parseHardwareArray(jsonArrayStr);
                    computer.setHardware(hardwareList);
                }
                jsonString = jsonString.substring(endIndex + 1).trim();
            } else if (jsonString.startsWith("{")) {
                int endIndex = findMatchingBracket(jsonString, '{', '}');
                jsonString = jsonString.substring(endIndex + 1).trim();
            } else {
                jsonString = jsonString.contains(",") ? jsonString.substring(jsonString.indexOf(",") + 1) : "";
            }
            if (jsonString.startsWith(",")) {
                jsonString = jsonString.substring(1).trim();
            }
        }
        return computer;
    }

    private static int findMatchingBracket(String str, char openBracket, char closeBracket) {
        int depth = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == openBracket) depth++;
            else if (c == closeBracket) depth--;
            if (depth == 0) return i;
        }
        return -1;
    }

    private static List<Hardware> parseHardwareArray(String jsonArrayStr) {
        List<Hardware> hardwareList = new ArrayList<>();
        jsonArrayStr = jsonArrayStr.trim().replaceAll("^\\[|]$", "");
        while (!jsonArrayStr.isEmpty()) {
            int bracketIndex = findMatchingBracket(jsonArrayStr, '{', '}');
            String objectStr = jsonArrayStr.substring(0, bracketIndex + 1);
            Hardware hardware = parseHardwareObject(objectStr);
            hardwareList.add(hardware);
            jsonArrayStr = jsonArrayStr.substring(bracketIndex + 1).trim();
            if (jsonArrayStr.startsWith(",")) {
                jsonArrayStr = jsonArrayStr.substring(1).trim();
            }
        }
        return hardwareList;
    }

    private static Hardware parseHardwareObject(String objectStr) {
        Hardware hardware = new Hardware();
        objectStr = objectStr.trim().replaceAll("^\\{|}$", "");
        while (!objectStr.isEmpty()) {
            int colonIndex = objectStr.indexOf(":");
            String key = objectStr.substring(0, colonIndex).trim().replaceAll("\"", "");
            objectStr = objectStr.substring(colonIndex + 1).trim();
            if (objectStr.startsWith("\"")) {
                int quoteEndIndex = objectStr.indexOf("\"", 1);
                String value = objectStr.substring(1, quoteEndIndex);
                switch (key) {
                    case "hardwareType":
                        hardware.setHardwareType(value);
                        break;
                    case "name":
                        hardware.setName(value);
                        break;
                }
                objectStr = objectStr.substring(quoteEndIndex + 1).trim();
            } else if (objectStr.startsWith("[")) {
                int endIndex = findMatchingBracket(objectStr, '[', ']');
                String jsonArrayStr = objectStr.substring(0, endIndex + 1);
                if ("subHardware".equals(key)) {
                    List<Hardware> subHardware = parseHardwareArray(jsonArrayStr);
                    hardware.setSubHardware(subHardware);
                } else if ("sensors".equals(key)) {
                    List<Sensor> sensors = parseSensorArray(jsonArrayStr);
                    hardware.setSensors(sensors);
                }
                objectStr = objectStr.substring(endIndex + 1).trim();
            }
            if (objectStr.startsWith(",")) {
                objectStr = objectStr.substring(1).trim();
            }
        }
        return hardware;
    }

    private static List<Sensor> parseSensorArray(String jsonArrayStr) {
        List<Sensor> sensorList = new ArrayList<>();
        jsonArrayStr = jsonArrayStr.trim().replaceAll("^\\[|]$", "");
        while (!jsonArrayStr.isEmpty()) {
            int bracketIndex = findMatchingBracket(jsonArrayStr, '{', '}');
            String objectStr = jsonArrayStr.substring(0, bracketIndex + 1);
            Sensor sensor = parseSensorObject(objectStr);
            sensorList.add(sensor);
            jsonArrayStr = jsonArrayStr.substring(bracketIndex + 1).trim();
            if (jsonArrayStr.startsWith(",")) {
                jsonArrayStr = jsonArrayStr.substring(1).trim();
            }
        }
        return sensorList;
    }

    private static Sensor parseSensorObject(String objectStr) {
        Sensor sensor = new Sensor();
        objectStr = objectStr.trim().replaceAll("^\\{|}$", "");
        while (!objectStr.isEmpty()) {
            int colonIndex = objectStr.indexOf(":");
            String key = objectStr.substring(0, colonIndex).trim().replaceAll("\"", "");
            objectStr = objectStr.substring(colonIndex + 1).trim();
            if (objectStr.startsWith("\"")) {
                int quoteEndIndex = objectStr.indexOf("\"", 1);
                String value = objectStr.substring(1, quoteEndIndex);
                switch (key) {
                    case "sensorType":
                        sensor.setSensorType(value);
                        break;
                    case "name":
                        sensor.setName(value);
                        break;
                }
                objectStr = objectStr.substring(quoteEndIndex + 1).trim();
            } else if (objectStr.startsWith("[")) {
                objectStr = objectStr.substring(findMatchingBracket(objectStr, '[', ']') + 1).trim();
            } else {
                double value = Double.parseDouble(objectStr.split(",")[0].trim());
                sensor.setValue(value);
                objectStr = objectStr.contains(",") ? objectStr.substring(objectStr.indexOf(",") + 1) : "";
            }
            if (objectStr.startsWith(",")) {
                objectStr = objectStr.substring(1).trim();
            }
        }
        return sensor;
    }
}
