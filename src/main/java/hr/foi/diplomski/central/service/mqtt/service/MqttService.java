package hr.foi.diplomski.central.service.mqtt.service;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttService {

    void updateSensorName(String sensorId, String newName) throws MqttException;

    void updateSensorIntervals(String sensorId, Integer beaconDataPurgeInterval, Integer beaconDataSendInterval) throws MqttException;

    void updateAllSensorsIntervals(Integer beaconDataPurgeInterval, Integer beaconDataSendInterval) throws MqttException;
}
