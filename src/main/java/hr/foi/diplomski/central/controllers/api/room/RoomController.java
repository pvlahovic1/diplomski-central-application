package hr.foi.diplomski.central.controllers.api.room;

import hr.foi.diplomski.central.controllers.api.room.data.RoomDto;
import hr.foi.diplomski.central.controllers.api.room.data.RoomViewDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.service.rooms.RoomService;
import hr.foi.diplomski.central.service.sensors.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final SensorService sensorService;

    @GetMapping
    public ResponseEntity<List<RoomViewDto>> getAllRoomsView() {
        return ResponseEntity.ok(roomService.getAllRoomsView());
    }

    @GetMapping("/{id}/sensors")
    public ResponseEntity<List<SensorViewDto>> getAllSensorsInRoom(@PathVariable Long id) {
        return ResponseEntity.ok(sensorService.getAllSensorsViewByRoom(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RoomDto> saveNewRoom(@Valid @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.saveRoom(roomDto));
    }

}
