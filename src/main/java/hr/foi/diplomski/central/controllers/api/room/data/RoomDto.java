package hr.foi.diplomski.central.controllers.api.room.data;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoomDto {

    private Long id;
    @NotNull
    @Length(max = 255)
    private String name;
    @NotNull
    @Min(1)
    private Double length;
    @NotNull
    @Min(1)
    private Double width;
    @NotNull
    @Min(1)
    private Double height;
    private List<SensorViewDto> sensors;

}
