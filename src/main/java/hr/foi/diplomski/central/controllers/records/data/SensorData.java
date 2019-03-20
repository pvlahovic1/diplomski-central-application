package hr.foi.diplomski.central.controllers.records.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorData {

    @NotNull
    @Length(min = 64, max = 64)
    private String deviceId;

    @NotEmpty
    List<SensorRecord> sensorRecords;

}
