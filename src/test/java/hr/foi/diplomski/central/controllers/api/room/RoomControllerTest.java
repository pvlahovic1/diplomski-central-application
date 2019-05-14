package hr.foi.diplomski.central.controllers.api.room;

import hr.foi.diplomski.central.CentralApplication;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceInRoomDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;
import hr.foi.diplomski.central.controllers.api.room.data.RoomDto;
import hr.foi.diplomski.central.controllers.api.room.data.RoomViewDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.service.device.DeviceService;
import hr.foi.diplomski.central.service.rooms.RoomService;
import hr.foi.diplomski.central.service.sensors.SensorService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class RoomControllerTest {

    private static final String TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzZWN1cmUtYXBpIiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6InB2bGFob3ZpYyIsImV4cCI6MTU1ODcxNzAxNiwicm9sIjpbIlJPTEVfQURNSU4iLCJST0xFX1NFTlNPUiIsIlJPTEVfVVNFUiJdfQ.PJH8wJY4xEVka_MMH-9pFipknAXFWutRNT6prTYs4FgswhCDS5UnhfnFmTZREQJo2qW2bcMAI9xUo4h3BgTiYA";

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RoomService roomService;

    @MockBean
    private SensorService sensorService;

    @MockBean
    private DeviceService deviceService;

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
    public void returAllRoomViews_onGetAllRoomsView() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka prostorije"),
                fieldWithPath("[].itemName").description("Naziv prostorije"),
        };

        when(roomService.getAllRoomsView()).thenReturn(createRoomViewsDto());

        mvc.perform(get("/api/rooms").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("roomControllerGetAllRoomsView", responseFields(book)));

    }

    @Test
    public void returAllSensorsInRoom_onGetAllSensorsInRoom() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka senzora."),
                fieldWithPath("[].name").description("Naziv dohvaćenog senzora."),
                fieldWithPath("[].beaconDataPurgeInterval").description("Interval kojim senzor briše skenirane podatke."),
                fieldWithPath("[].beaconDataSendInterval").description("Interval kojim senzor šalje podatke na centralnu aplikaciju."),
                fieldWithPath("[].roomName").description("Naziv prostorije unutar koje se senzor nalazi."),
                fieldWithPath("[].present").description("Definira je li senzor trenutko dostupan.")
        };

        when(sensorService.getAllSensorsViewByRoom(any())).thenReturn(createSensorViewsDto(1));

        mvc.perform(get("/api/rooms/1/sensors").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("roomControllerGetAllSensorsInRoom", responseFields(book)));

    }

    @Test
    public void returnRoom_ongGetRoomById() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka prostorije."),
                fieldWithPath("name").description("Naziv dohvaćene prostorije."),
                fieldWithPath("width").description("Širina prostorije."),
                fieldWithPath("length").description("Duljina prostorije."),
                fieldWithPath("height").description("Visina prostorije."),
                fieldWithPath("sensors.[].id").description("Identifikacijska oznaka senzora koji se nalazi unutar prostorije."),
                fieldWithPath("sensors.[].name").description("Naziv senzora koji se nalazi unutar prostorije."),
                fieldWithPath("sensors.[].beaconDataPurgeInterval").description("Interval kojim, senzor koji se nalazi unutar prostorije, briše skenirane podatke."),
                fieldWithPath("sensors.[].beaconDataSendInterval").description("Interval kojim, senzor koji se nalazi unutar prostorije, šalje podatke na centralnu aplikaciju."),
                fieldWithPath("sensors.[].roomName").description("Naziv prostorije unutar koje se senzor nalazi."),
                fieldWithPath("sensors.[].present").description("Definira je li senzor trenutko dostupan.")
        };

        when(roomService.getById(any())).thenReturn(createRoomDto());

        mvc.perform(get("/api/rooms/1").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("roomControllerGetRoomById", responseFields(book)));

    }

    @Test
    public void returnAllDevicesInRoomDto_onGetAllDevicesInRoom() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].dateTime").description("Vrijeme kada je senzor poslao podatke o udaljenosti uređaja."),
                fieldWithPath("[].distance").description("Izračunata udaljenost uređaja od senzora."),
                fieldWithPath("[].deviceInfo").description("Dodatni podaci o uređaju koji se nalazi unutar prostorije."),
                fieldWithPath("[].deviceInfo.id").description("Identifikacijska oznaka uređaja koji se nalazi unutar prostorije."),
                fieldWithPath("[].deviceInfo.name").description("Naziv uređaja koji se nalazi unutar prostorije."),
                fieldWithPath("[].deviceInfo.beaconData").description("Podaci o beaconu koji definira uređaj koji se nalazi unutar prostorije.")
        };

        when(deviceService.findAllDevicesInRoom(any())).thenReturn(createDevicesInRoomDto());

        mvc.perform(get("/api/rooms/1/devices").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("roomControllerGetAllDevicesInRoom", responseFields(book)));

    }

    @Test
    public void returnRoomDto_onSaveNewRoom() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka pohranjene prostorije."),
                fieldWithPath("name").description("Naziv pohranjene prostorije."),
                fieldWithPath("width").description("Širina pohranjene prostorije."),
                fieldWithPath("length").description("Duljina pohranjene prostorije."),
                fieldWithPath("height").description("Visina pohranjene prostorije."),
                fieldWithPath("sensors.[].id").description("Identifikacijska oznaka senzora koji je pridružen pohranjene prostorije."),
                fieldWithPath("sensors.[].name").description("Naziv senzora koji je pridružen pohranjene prostorije."),
                fieldWithPath("sensors.[].beaconDataPurgeInterval").description("Interval kojim, senzor koji je pridružen pohranjene prostorije, briše skenirane podatke."),
                fieldWithPath("sensors.[].beaconDataSendInterval").description("Interval kojim, senzor koji je pridružen pohranjene prostorije, šalje podatke na centralnu aplikaciju."),
                fieldWithPath("sensors.[].roomName").description("Naziv prostorije."),
                fieldWithPath("sensors.[].present").description("Definira je li senzor trenutko dostupan.")
        };

        when(roomService.saveRoom(any())).thenReturn(createRoomDto());

        mvc.perform(post("/api/rooms").header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Prostorija 1\",\"length\":3.0,\"width\":5.0,\"height\":2.0,\"sensors\":[{\"id\":1},{\"id\":2}]}"))
                .andExpect(status().isOk())
                .andDo(document("roomControllerSaveNewRoom", responseFields(book)));

    }

    @Test
    public void deleteRoom_onDeleteRoom() throws Exception {
        doNothing().when(roomService).deleteRoom(any());

        mvc.perform(delete("/api/rooms/1").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("roomControllerDeleteRoom"));

    }

    private List<SensorViewDto> createSensorViewsDto(int roomNumber) {
        List<SensorViewDto> dtos = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            dtos.add(createSensorViewDto(i + 1, roomNumber));
        }

        return dtos;
    }

    private List<RoomViewDto> createRoomViewsDto() {
        List<RoomViewDto> rooms = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            rooms.add(createRoomViewDto(i + 1));
        }

        return rooms;
    }

    private List<DeviceInRoomDto> createDevicesInRoomDto() {
        List<DeviceInRoomDto> devices = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            devices.add(createDeviceInRoomDto(i + 1));
        }

        return devices;
    }

    private RoomDto createRoomDto() {
        RoomDto roomDto = new RoomDto();
        roomDto.setName("Prostorija 1");
        roomDto.setId(1L);
        roomDto.setWidth(5d);
        roomDto.setLength(3d);
        roomDto.setHeight(2d);
        roomDto.setSensors(createSensorViewsDto(1));

        return roomDto;
    }

    private RoomViewDto createRoomViewDto(int index) {
        RoomViewDto room = new RoomViewDto();
        room.setId((long) (index));
        room.setItemName(String.format("Prostorija %s", index));

        return room;
    }

    private DeviceInRoomDto createDeviceInRoomDto(int index) {
        DeviceInRoomDto dto = new DeviceInRoomDto();
        dto.setDeviceInfo(createDeviceViewDto(index));
        dto.setDateTime(LocalDateTime.now());
        dto.setDistance(0 + 5 * random.nextDouble());

        return dto;
    }

    private DeviceViewDto createDeviceViewDto(int index) {
        DeviceViewDto dto = new DeviceViewDto();
        dto.setId((long) index);
        dto.setName(String.format("Uređaj %s", index));
        dto.setBeaconData(String.format("B5B182C7-EAB1-4988-AA99-B5C1517008D9 1 %s", index));

        return dto;
    }

    private SensorViewDto createSensorViewDto(int index, int roomNumber) {
        SensorViewDto dto = new SensorViewDto();
        dto.setId((long) index);
        dto.setBeaconDataSendInterval(10);
        dto.setBeaconDataPurgeInterval(11);
        dto.setRoomName(String.format("Prostorija %s", roomNumber));
        dto.setName("Senzor " + index);
        dto.setPresent(index % 2 == 0);

        return dto;
    }

}
