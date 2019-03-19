package hr.foi.diplomski.central.controllers.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorRecord {

    @NotNull
    private String uuid;
    @NotNull
    @Min(1)
    private Integer major;
    @NotNull
    @Min(1)
    private Integer minor;
    @NotNull
    @Min(0)
    private Double distance;

}
