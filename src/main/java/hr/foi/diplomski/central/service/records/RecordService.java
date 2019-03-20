package hr.foi.diplomski.central.service.records;

import hr.foi.diplomski.central.controllers.records.data.SensorData;
import hr.foi.diplomski.central.exceptions.BadDataException;

public interface RecordService {

    void createNewRecord(SensorData sensorData) throws BadDataException;

}
