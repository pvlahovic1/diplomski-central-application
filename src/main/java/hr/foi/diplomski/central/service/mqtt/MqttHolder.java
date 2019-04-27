package hr.foi.diplomski.central.service.mqtt;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import javax.annotation.PreDestroy;

@Data
public class MqttHolder {

    private MqttClient mqttClient;

    public MqttHolder(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @PreDestroy
    public void disconnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
