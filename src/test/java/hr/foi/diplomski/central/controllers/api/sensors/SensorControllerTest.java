package hr.foi.diplomski.central.controllers.api.sensors;

import hr.foi.diplomski.central.CentralApplication;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.service.sensors.SensorService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

import static hr.foi.diplomski.central.SecurityUtilsTest.TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CentralApplication.class)
public class SensorControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private SensorService sensorService;

    private MockMvc mvc;

    private Random random;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(springSecurity())
                .apply(documentationConfiguration(this.restDocumentation))
                .build();

        random = new Random(System.currentTimeMillis());
    }

    @Test
    public void returSensor_onUpdateSensorData() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka senzora."),
                fieldWithPath("sensorId").description("Sažetak podataka o senzoru prilikom kreiranja."),
                fieldWithPath("sensorName").description("Naziv senzora"),
                fieldWithPath("beaconDataSendInterval").description("Interval kojim senzor šalje podatke o skeniranim beaconima."),
                fieldWithPath("beaconDataPurgeInterval").description("Interval kojim senzor briše podatke o skeniranim beaconima."),
                fieldWithPath("lastTimePresent").description("Vrijeme kada je senzor zadnji puta bio aktivan")
        };

        when(sensorService.updateSensor(any())).thenReturn(createSensor(1));

        mvc.perform(put("/api/sensors").header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceId\":\"2bc0bd3d00ef18d02398d4e4b2019857e1682102607c4dd5fbf8912fada254ee\",\"deviceName\":\"Rapbperry Pi 1\",\"beaconDataPurgeInterval\":10000,\"beaconDataSendInterval\":11000}".getBytes()))
                .andExpect(status().isOk())
                .andDo(document("sensorControllerUpdateSensorData", responseFields(book)));
    }

    @Test
    public void returnSensorViewDtos_onGetAllSensors() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka senzora."),
                fieldWithPath("[].name").description("Naziv senzora."),
                fieldWithPath("[].beaconDataSendInterval").description("Interval kojim senzor šalje podatke o skeniranim beaconima."),
                fieldWithPath("[].beaconDataPurgeInterval").description("Interval kojim senzor briše podatke o skeniranim beaconima."),
                fieldWithPath("[].roomName").description("Naziv prostorije unutar koje se senzor nalazi."),
                fieldWithPath("[].present").description("Vrijednost koja definira je li senzor trenutno dostupan.")
        };

        when(sensorService.getAllSensors()).thenReturn(createSensorViewDtos());

        mvc.perform(get("/api/sensors").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("sensorControllerGetAllSensors", responseFields(book)));
    }

    @Test
    public void returnSensorDto_onSaveSensor() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka senzora."),
                fieldWithPath("name").description("Naziv senzora."),
                fieldWithPath("beaconDataSendInterval").description("Interval kojim senzor šalje podatke o skeniranim beaconima."),
                fieldWithPath("beaconDataPurgeInterval").description("Interval kojim senzor briše podatke o skeniranim beaconima."),
                fieldWithPath("present").description("Vrijednost koja definira je li senzor trenutno dostupan.")
        };

        when(sensorService.saveSensor(any())).thenReturn(createSensorDto(1));

        mvc.perform(post("/api/sensors").header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":null, \"name\":\"Raspberry Pi 1\", \"beaconDataPurgeInterval\": 11, \"beaconDataSendInterval\": 10, \"present\":false}".getBytes()))
                .andExpect(status().isOk())
                .andDo(document("sensorControllerSaveSensor", responseFields(book)));
    }

    @Test
    public void returnSensorDto_onGetSensorById() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka senzora."),
                fieldWithPath("name").description("Naziv senzora."),
                fieldWithPath("beaconDataSendInterval").description("Interval kojim senzor šalje podatke o skeniranim beaconima."),
                fieldWithPath("beaconDataPurgeInterval").description("Interval kojim senzor briše podatke o skeniranim beaconima."),
                fieldWithPath("present").description("Vrijednost koja definira je li senzor trenutno dostupan.")
        };

        when(sensorService.getSensordById(any())).thenReturn(createSensorDto(1));

        mvc.perform(get("/api/sensors/1").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("sensorControllerGetSensorById", responseFields(book)));
    }

    @Test
    public void returnSensorViewDtos_onGetAllFreeSensors() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka senzora."),
                fieldWithPath("[].name").description("Naziv senzora."),
                fieldWithPath("[].beaconDataSendInterval").description("Interval kojim senzor šalje podatke o skeniranim beaconima."),
                fieldWithPath("[].beaconDataPurgeInterval").description("Interval kojim senzor briše podatke o skeniranim beaconima."),
                fieldWithPath("[].roomName").description("Naziv prostorije unutar koje se senzor nalazi."),
                fieldWithPath("[].present").description("Vrijednost koja definira je li senzor trenutno dostupan.")
        };

        List<SensorViewDto> dtos = createSensorViewDtos();
        for (SensorViewDto dto : dtos) {
            dto.setRoomName("Nije postavljena");
        }

        when(sensorService.getAllFreeSensors()).thenReturn(dtos);

        mvc.perform(get("/api/sensors/free").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("sensorControllerGetAllFreeSensors", responseFields(book)));
    }

    @Test
    public void returnConfiguration_onCreateConfigurationForSensor() throws Exception {
        when(sensorService.createCongifurationFile(any())).thenReturn(createHttpResponse());

        mvc.perform(get("/api/sensors/1/configuration").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("sensorControllerCreateConfigurationForSensor"));
    }

    @Test
    public void deleteSensor_onDeleteSensor() throws Exception {
        doNothing().when(sensorService).deleteSensor(any());

        mvc.perform(delete("/api/sensors/1").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("sensorControllerDeleteSensor"));

    }

    private List<SensorViewDto> createSensorViewDtos() {
        List<SensorViewDto> dtos = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            dtos.add(createSensorViewDto(i + 1));
        }

        return dtos;
    }

    private SensorDto createSensorDto(int index) {
        SensorDto sensorDto = new SensorDto();
        sensorDto.setId((long) index);
        sensorDto.setName("Raspberry Pi 1");
        sensorDto.setBeaconDataSendInterval(10);
        sensorDto.setBeaconDataPurgeInterval(11);
        sensorDto.setPresent(false);

        return sensorDto;
    }

    private SensorViewDto createSensorViewDto(int index) {
        SensorViewDto dto = new SensorViewDto();
        dto.setId((long) index);
        dto.setName("Raspberry Pi 1");
        dto.setBeaconDataSendInterval(10);
        dto.setBeaconDataPurgeInterval(11);
        dto.setRoomName(String.format("Room%s", index));
        dto.setPresent(true);

        return dto;
    }

    private Sensor createSensor(int index) {
        Sensor sensor = new Sensor();
        sensor.setId((long) index);
        sensor.setSensorId("2bc0bd3d00ef18d02398d4e4b2019857e1682102607c4dd5fbf8912fada254ee");
        sensor.setBeaconDataSendInterval(10000);
        sensor.setBeaconDataPurgeInterval(11000);
        sensor.setLastTimePresent(LocalDateTime.now());
        sensor.setSensorName("Rapbperry Pi 1");

        return sensor;
    }

    private HttpEntity<byte[]> createHttpResponse() {
        StringJoiner stringJoiner = new StringJoiner("\n");

        stringJoiner.add(String.format("deviceId=%s", "2bc0bd3d00ef18d02398d4e4b2019857e1682102607c4dd5fbf8912fada254ee"));
        stringJoiner.add(String.format("deviceName=%s", "Rapbperry Pi 1"));
        stringJoiner.add(String.format("username=%s", "sensor_device"));
        stringJoiner.add(String.format("password=%s", "sensor_device_password"));
        stringJoiner.add(String.format("beaconDataPurgeInterval=%s", 11000));
        stringJoiner.add(String.format("beaconDataSendInterval=%s", 10000));
        stringJoiner.add(String.format("mqttTopicUrl=%s", "tcp://153.92.209.230:1883"));
        stringJoiner.add(String.format("mqttTopicTitle=%s", "listener_settings"));
        stringJoiner.add(String.format("centralApplicationUrl=%s", "http://localhost:8080"));
        stringJoiner.add(String.format("centralApplicationBeaconPath=%s", "/api/records"));
        stringJoiner.add(String.format("centralApplicationDevicePath=%s", "/api/sensors"));
        stringJoiner.add(String.format("centralApplicationAuthenticationPath=%s", "/api/authenticate"));

        byte[] document = stringJoiner.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.TEXT_PLAIN);
        header.set("Content-Disposition", "inline; filename=" + "listener_configuration.conf");
        header.setContentLength(document.length);

        return new HttpEntity<>(document, header);
    }

}
