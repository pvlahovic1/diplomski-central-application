package hr.foi.diplomski.central.service.rooms;

import hr.foi.diplomski.central.controllers.api.room.data.RoomDto;
import hr.foi.diplomski.central.controllers.api.room.data.RoomViewDto;

import java.util.List;

public interface RoomService {

    RoomDto saveRoom(RoomDto roomDto);

    List<RoomViewDto> getAllRoomsView();

}
