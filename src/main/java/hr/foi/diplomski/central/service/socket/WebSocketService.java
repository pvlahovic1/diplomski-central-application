package hr.foi.diplomski.central.service.socket;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate webSocket;

    public void refreshRoomsState(List<Long> roomIds) {
        webSocket.convertAndSend("/topic/room", new Gson().toJson(roomIds));
    }

    public void refreshSensorState(Long sensorId) {
        webSocket.convertAndSend("/topic/sensor", String.valueOf(sensorId));
    }
}
