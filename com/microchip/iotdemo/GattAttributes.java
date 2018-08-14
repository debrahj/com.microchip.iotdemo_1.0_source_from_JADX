package com.microchip.iotdemo;

import java.util.HashMap;

public class GattAttributes {
    public static final String ALERT_NOTIFICATION_SERVICE = "00001811-0000-1000-8000-00805f9b34fb";
    public static final String APPEARANCE_STRING = "00002a01-0000-1000-8000-00805f9b34fb";
    public static final String BATTERY_LEVEL = "00002a19-0000-1000-8000-00805f9b34fb";
    public static final String BATTERY_SERVICE = "0000180f-0000-1000-8000-00805f9b34fb";
    public static final String BLOOD_PRESSURE_SERVICE = "00001810-0000-1000-8000-00805f9b34fb";
    public static final String CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";
    public static final String CURRENT_TIME_SERVICE = "00001805-0000-1000-8000-00805f9b34fb";
    public static final String CYCLING_POWER_SERVICE = "00001818-0000-1000-8000-00805f9b34fb";
    public static final String CYCLING_SPEED_AND_CADENCE_SERVICE = "00001816-0000-1000-8000-00805f9b34fb";
    public static final String DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb";
    public static final String DEVICE_NAME_STRING = "00002a00-0000-1000-8000-00805f9b34fb";
    public static final String FIRMWARE_REVISION = "00002a26-0000-1000-8000-00805f9b34fb";
    public static final String GENERIC_ACCESS_SERVICE = "00001800-0000-1000-8000-00805f9b34fb";
    public static final String GENERIC_ATTRIBUTE_SERVICE = "00001801-0000-1000-8000-00805f9b34fb";
    public static final String GLUCOSE_SERVICE = "00001808-0000-1000-8000-00805f9b34fb";
    public static final String HARDWARE_REVISION = "00002a27-0000-1000-8000-00805f9b34fb";
    public static final String HEALTH_THERMOMETER_SERVICE = "00001809-0000-1000-8000-00805f9b34fb";
    public static final String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static final String HEART_RATE_SERVICE = "0000180d-0000-1000-8000-00805f9b34fb";
    public static final String HUMAN_INTERFACE_DEVICE_SERVICE = "00001812-0000-1000-8000-00805f9b34fb";
    public static final String IMMEDIATE_ALERT_SERVICE = "00001802-0000-1000-8000-00805f9b34fb";
    public static final String LINK_LOSS_SERVICE = "00001803-0000-1000-8000-00805f9b34fb";
    public static final String LOCATION_AND_NAVIGATION_SERVICE = "00001819-0000-1000-8000-00805f9b34fb";
    public static final String MANUFACTURER_NAME = "00002a29-0000-1000-8000-00805f9b34fb";
    public static final String MLDP_CONTROL_PRIVATE_CHAR = "00035b03-58e6-07dd-021a-08123a0003ff";
    public static final String MLDP_DATA_PRIVATE_CHAR = "00035b03-58e6-07dd-021a-08123a000301";
    public static final String MLDP_PRIVATE_SERVICE = "00035b03-58e6-07dd-021a-08123a000300";
    public static final String MODEL_NUMBER = "00002a24-0000-1000-8000-00805f9b34fb";
    public static final String NEXT_DST_CHANGE_SERVICE = "00001807-0000-1000-8000-00805f9b34fb";
    public static final String PHONE_ALERT_STATUS_SERVICE = "0000180e-0000-1000-8000-00805f9b34fb";
    public static final String PLUG_N_PLAY_ID = "00002a50-0000-1000-8000-00805f9b34fb";
    public static final String PREFERRED_PARAMETERS = "00002a04-0000-1000-8000-00805f9b34fb";
    public static final String PUMP_MONITOR_PRIVATE_CHAR1 = "01020304-0506-0708-0900-0a0b0c0d0e0f";
    public static final String PUMP_MONITOR_PRIVATE_CHAR2 = "01020304-0506-0708-0900-0a0b0c0d0e1f";
    public static final String PUMP_MONITOR_PRIVATE_CHAR3 = "01020304-0506-0708-0900-0a0b0c0d0e2f";
    public static final String PUMP_MONITOR_PRIVATE_CHAR4 = "01020304-0506-0708-0900-0a0b0c0d0e3f";
    public static final String PUMP_MONITOR_PRIVATE_CHAR5 = "01020304-0506-0708-0900-0a0b0c0d0e4f";
    public static final String PUMP_MONITOR_PRIVATE_SERVICE = "11223344-5566-7788-9900-aabbccddeeff";
    public static final String REFERENCE_TIME_UPDATE_SERVICE = "00001806-0000-1000-8000-00805f9b34fb";
    public static final String REGULATORY_DATA = "00002a2a-0000-1000-8000-00805f9b34fb";
    public static final String RUNNING_SPEED_AND_CADENCE_SERVICE = "00001814-0000-1000-8000-00805f9b34fb";
    public static final String SCAN_PARAMETERS_SERVICE = "00001813-0000-1000-8000-00805f9b34fb";
    public static final String SERIAL_NUMBER = "00002a25-0000-1000-8000-00805f9b34fb";
    public static final String SOFTWARE_REVISION = "00002a28-0000-1000-8000-00805f9b34fb";
    public static final String SYSTEM_ID = "00002a23-0000-1000-8000-00805f9b34fb";
    public static final String TX_POWER_SERVICE = "00001804-0000-1000-8000-00805f9b34fb";
    private static HashMap<String, String> attributes;

    public GattAttributes() {
        GattAttributes gattAttributes = this;
    }

    static {
        HashMap hashMap = r3;
        HashMap hashMap2 = new HashMap();
        attributes = hashMap;
        Object put = attributes.put(GENERIC_ACCESS_SERVICE, "Generic Access Service");
        put = attributes.put(GENERIC_ATTRIBUTE_SERVICE, "Generic Attribute Service");
        put = attributes.put(IMMEDIATE_ALERT_SERVICE, "Immediate Alert Service");
        put = attributes.put(LINK_LOSS_SERVICE, "Link Loss Service");
        put = attributes.put(TX_POWER_SERVICE, "Tx Power Service");
        put = attributes.put(CURRENT_TIME_SERVICE, "Current Time Service");
        put = attributes.put(REFERENCE_TIME_UPDATE_SERVICE, "Reference Time Update Service");
        put = attributes.put(NEXT_DST_CHANGE_SERVICE, "Next DST Change Service");
        put = attributes.put(GLUCOSE_SERVICE, "Glucose Service");
        put = attributes.put(HEALTH_THERMOMETER_SERVICE, "Health Thermometer Service");
        put = attributes.put(DEVICE_INFORMATION_SERVICE, "Device Info Service");
        put = attributes.put(HEART_RATE_SERVICE, "Heart Rate Service");
        put = attributes.put(PHONE_ALERT_STATUS_SERVICE, "Phone Alert Status Service");
        put = attributes.put(BATTERY_SERVICE, "Battery Service");
        put = attributes.put(BLOOD_PRESSURE_SERVICE, "Blood Pressure Service");
        put = attributes.put(ALERT_NOTIFICATION_SERVICE, "Alert Notification Service");
        put = attributes.put(HUMAN_INTERFACE_DEVICE_SERVICE, "Human Interface Device Service");
        put = attributes.put(SCAN_PARAMETERS_SERVICE, "Scan Parameters Service");
        put = attributes.put(RUNNING_SPEED_AND_CADENCE_SERVICE, "Running Speed and Cadence Service");
        put = attributes.put(CYCLING_SPEED_AND_CADENCE_SERVICE, "Cycling Speed and Cadence Service");
        put = attributes.put(CYCLING_POWER_SERVICE, "Cycling Power Service");
        put = attributes.put(LOCATION_AND_NAVIGATION_SERVICE, "Location and Navigation Service");
        put = attributes.put(MLDP_PRIVATE_SERVICE, "MLDP Service");
        put = attributes.put(PUMP_MONITOR_PRIVATE_SERVICE, "Pump Monitor Service");
        put = attributes.put(DEVICE_NAME_STRING, "Device Name String");
        put = attributes.put(APPEARANCE_STRING, "Appearance String");
        put = attributes.put(PREFERRED_PARAMETERS, "Preferred Parameters");
        put = attributes.put(BATTERY_LEVEL, "Battery Level");
        put = attributes.put(SYSTEM_ID, "System ID");
        put = attributes.put(MODEL_NUMBER, "Model Number");
        put = attributes.put(SERIAL_NUMBER, "Serial Number");
        put = attributes.put(FIRMWARE_REVISION, "Firmware Revision");
        put = attributes.put(HARDWARE_REVISION, "Hardware Revision");
        put = attributes.put(SOFTWARE_REVISION, "Software Revision");
        put = attributes.put(MANUFACTURER_NAME, "Manufacturer Name");
        put = attributes.put(REGULATORY_DATA, "Regulatory Certification Data List");
        put = attributes.put(PLUG_N_PLAY_ID, "Plug-n-Play ID");
        put = attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        put = attributes.put(MLDP_DATA_PRIVATE_CHAR, "MLDP Data");
        put = attributes.put(MLDP_CONTROL_PRIVATE_CHAR, "MLDP Conrtrol");
        put = attributes.put(PUMP_MONITOR_PRIVATE_CHAR1, "Pump Pressure");
        put = attributes.put(PUMP_MONITOR_PRIVATE_CHAR2, "Pump Fluid Temp");
        put = attributes.put(PUMP_MONITOR_PRIVATE_CHAR3, "Pump Voltage");
        put = attributes.put(PUMP_MONITOR_PRIVATE_CHAR4, "Pump Current");
        put = attributes.put(PUMP_MONITOR_PRIVATE_CHAR5, "Pump ID");
    }

    public static String lookup(String str, String str2) {
        String name = (String) attributes.get(str);
        return name == null ? str2 : name;
    }
}
