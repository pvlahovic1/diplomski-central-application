package hr.foi.diplomski.central.service.mqtt.service;

import hr.foi.diplomski.central.service.mqtt.MqttHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttServiceImpl implements MqttService {

    @Value("${mqttTopicTitle}")
    private String mqttTopicTitle;

    private final MqttHolder mqttHolder;

    @Override
    public void updateSensorName(String sensorId, String newName) throws MqttException {
        sendMqttMessage(String.format("DEVICE_ID %s;SET_NAME %s;", sensorId, newName));
    }

    @Override
    public void updateSensorIntervals(String sensorId, Integer beaconDataPurgeInterval, Integer beaconDataSendInterval) throws MqttException {
        sendMqttMessage(String.format("DEVICE_ID %s;PURING_INTERVAL %s;SEND_INTERVAL %s;", sensorId,
                beaconDataPurgeInterval, beaconDataSendInterval));
    }

    @Override
    public void updateAllSensorsIntervals(Integer beaconDataPurgeInterval, Integer beaconDataSendInterval) throws MqttException {
        sendMqttMessage(String.format("DEVICE_ID ALL;PURING_INTERVAL %s;SEND_INTERVAL %s;", beaconDataPurgeInterval,
                beaconDataSendInterval));
    }

    private void sendMqttMessage(String messageData) throws MqttException {
        if (mqttHolder.getMqttClient().isConnected()) {
            MqttMessage message = new MqttMessage();
            message.setPayload(messageData.getBytes());
            mqttHolder.getMqttClient().publish(mqttTopicTitle, message);

            log.info("Sending message [ {} ] to mqtt topic", message);
        } else {
            log.warn("Mqtt client is not connected!");
        }
    }
}
