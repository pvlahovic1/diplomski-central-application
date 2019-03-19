package hr.foi.diplomski.central.service;

import hr.foi.diplomski.central.controllers.data.SensorData;
import hr.foi.diplomski.central.exceptions.BadDataException;

public interface RecordService {

    void createNewRecord(SensorData sensorData) throws BadDataException;

}
