package hr.foi.diplomski.central.service.mqtt;

import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.repository.SensorRepository;
import hr.foi.diplomski.central.service.socket.WebSocketService;
import hr.foi.diplomski.central.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class MqttListenerCallBack implements MqttCallback {

    private final SensorRepository sensorRepository;
    private final WebSocketService webSocketService;

    @Override
    public void connectionLost(Throwable throwable) {
        log.error("Mqtt connection Lost: {}", throwable);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        try {
            String message = mqttMessage.toString();

            log.info("Mqtt messageArive: {}", message);

            if (CommonUtils.isSyntaxValid(CommonUtils.DEVICE_ACTIVITY_PROOF_MESSAGE, message)) {
                String deviceId = CommonUtils.dataFromSyntax(CommonUtils.DEVICE_ACTIVITY_PROOF_MESSAGE, message).get(0);
                var sensorOptional = sensorRepository.findBySensorId(deviceId);

                if (sensorOptional.isPresent()) {
                    Sensor sensor = sensorOptional.get();
                    sensor.setLastTimePresent(LocalDateTime.now());
                    sensorRepository.save(sensor);
                    log.info("Mqtt message is valid. Updating device: {}", deviceId);
                    webSocketService.refreshSensorState(sensor.getId());
                } else {
                    log.info("Mqtt message is valid. But device id {} doesent exists", deviceId);
                }
            }
        } catch (Exception e) {
            log.error("Error while processing mqtt message: ", e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("Mqtt: deliveryComplete");
    }

}
