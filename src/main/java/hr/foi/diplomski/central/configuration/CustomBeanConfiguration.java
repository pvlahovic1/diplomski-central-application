package hr.foi.diplomski.central.configuration;

import com.google.gson.Gson;
import hr.foi.diplomski.central.repository.SensorRepository;
import hr.foi.diplomski.central.service.mqtt.MqttHolder;
import hr.foi.diplomski.central.service.mqtt.MqttListenerCallBack;
import hr.foi.diplomski.central.service.socket.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class CustomBeanConfiguration {

    @Value("${mqttTopicUrl}")
    private String mqttTopicUrl;

    @Value("${mqttTopicTitle}")
    private String mqttTopicTitle;

    private final SensorRepository sensorRepository;
    private final WebSocketService webSocketService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public MqttHolder createMqttHolder() {
        MqttHolder mqttHolder = null;
        try {
            MqttListenerCallBack mqttListenerCallBack = new MqttListenerCallBack(sensorRepository, webSocketService);
            MqttClient client = new MqttClient(mqttTopicUrl, MqttClient.generateClientId());
            client.setCallback(mqttListenerCallBack);
            client.connect();
            client.subscribe(mqttTopicTitle);
            mqttHolder = new MqttHolder(client);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return mqttHolder;
    }

    @Bean
    public RestTemplate createRestTemplate() {
        return  restTemplateBuilder.build();
    }

    @Bean
    public Gson createGson() {
        return new Gson();
    }

}
