package hr.foi.diplomski.central.service.records;

import hr.foi.diplomski.central.controllers.api.records.data.SensorData;

public interface RecordService {

    void createNewRecord(SensorData sensorData);

}
