package hr.foi.diplomski.central.mappers.reslovers;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SensorResolver {

    private final SensorRepository sensorRepository;

    @ObjectFactory
    public Sensor resolve(SensorViewDto dto, @TargetType Class<Sensor> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? sensorRepository.findById(dto.getId()).get() : new Sensor();
    }

}
