package hr.foi.diplomski.central.configuration;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CustomBeanConfiguration {

    @Value("${mqttTopicUrl}")
    private String mqttTopicUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public MqttClient createMqttClient() throws MqttException {
        return new MqttClient(mqttTopicUrl, MqttClient.generateClientId());
    }

}
