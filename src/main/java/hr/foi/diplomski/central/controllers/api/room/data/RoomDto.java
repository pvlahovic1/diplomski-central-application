package hr.foi.diplomski.central.controllers.api.room.data;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoomDto {

    private Long id;
    @NotNull
    @Length(max = 255)
    private String name;
    private List<SensorViewDto> sensors;

}
