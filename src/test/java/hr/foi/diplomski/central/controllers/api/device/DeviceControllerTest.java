package hr.foi.diplomski.central.controllers.api.device;

import com.google.gson.Gson;
import hr.foi.diplomski.central.CentralApplication;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceSaveDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;
import hr.foi.diplomski.central.service.device.DeviceService;
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

import java.util.ArrayList;
import java.util.List;

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
public class DeviceControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private DeviceService deviceService;

    private MockMvc mvc;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(springSecurity())
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void returnDeviceViewDtos_onGetAllDevices() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka uređaja."),
                fieldWithPath("[].name").description("Naziv uređaja."),
                fieldWithPath("[].beaconData").description("Podaci o beaconu koji je postavljen na uređaj."),
        };

        when(deviceService.findAllDevices()).thenReturn(createDeviceViewDtos());

        mvc.perform(get("/api/devices").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("deviceControllerGetAllDevices", responseFields(book)));
    }

    @Test
    public void returnDeviceViewDtos_onGetAllFreeDevices() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka uređaja."),
                fieldWithPath("[].name").description("Naziv uređaja."),
                fieldWithPath("[].beaconData").description("Podaci o beaconu koji je postavljen na uređaj."),
        };

        List<DeviceViewDto> dtos = createDeviceViewDtos();
        for (DeviceViewDto dto : dtos) {
            dto.setBeaconData("Beacon nije postavljen");
        }

        when(deviceService.findAllFreeDevices()).thenReturn(dtos);

        mvc.perform(get("/api/devices/free").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("deviceControllerGetAllFreeDevices", responseFields(book)));
    }

    @Test
    public void returnDeviceSaveDto_onGetDeviceById() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka uređaja."),
                fieldWithPath("deviceName").description("Naziv uređaja."),
                fieldWithPath("beaconView").description("Podaci o beaconu koji je postavljen na uređaj."),
                fieldWithPath("beaconView.id").description("Identifikacijska oznaka beacona koji je postavljen na uređaj."),
                fieldWithPath("beaconView.itemName").description("Podaci o beaconu koji je postavljen na uređaj."),
        };

        when(deviceService.findById(any())).thenReturn(createDeviceSaveDto(1));

        mvc.perform(get("/api/devices/1").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("deviceControllerGetDeviceById", responseFields(book)));
    }

    @Test
    public void returnDeviceSaveDto_onSaveDevice() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka uređaja."),
                fieldWithPath("deviceName").description("Naziv uređaja."),
                fieldWithPath("beaconView").description("Podaci o beaconu koji je postavljen na uređaj."),
                fieldWithPath("beaconView.id").description("Identifikacijska oznaka beacona koji je postavljen na uređaj."),
                fieldWithPath("beaconView.itemName").description("Podaci o beaconu koji je postavljen na uređaj."),
        };

        when(deviceService.saveDevice(any())).thenReturn(createDeviceSaveDto(1));

        DeviceSaveDto dto = createDeviceSaveDto(1);
        dto.setId(null);

        mvc.perform(post("/api/devices").header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(dto)))
                .andExpect(status().isOk())
                .andDo(document("deviceControllerSaveDevice", responseFields(book)));
    }

    @Test
    public void deleteDevice_onDeleteDevice() throws Exception {
        doNothing().when(deviceService).deleteDevice(any());

        mvc.perform(delete("/api/devices/1").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("deviceControllerDeleteDevice"));
    }

    private List<DeviceViewDto> createDeviceViewDtos() {
        List<DeviceViewDto> dtos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            dtos.add(createDeviceViewDto(i + 1));
        }

        return dtos;
    }

    private DeviceViewDto createDeviceViewDto(int index) {
        DeviceViewDto dto = new DeviceViewDto();
        dto.setId((long) index);
        dto.setBeaconData(String.format("B5B182C7-EAB1-4988-AA99-B5C1517008D9 1 %s", index));
        dto.setName(String.format("Device %s", index));

        return dto;
    }

    private DeviceSaveDto createDeviceSaveDto(int index) {
        DeviceSaveDto dto = new DeviceSaveDto();
        dto.setId((long) index);
        dto.setDeviceName(String.format("Device %s", index));
        dto.setBeaconView(createBeaconViewDto(index));

        return dto;
    }

    private BeaconViewDto createBeaconViewDto(int index) {
        BeaconViewDto dto = new BeaconViewDto();
        dto.setId((long) index);
        dto.setItemName(String.format("B5B182C7-EAB1-4988-AA99-B5C1517008D9 1 %s", index));

        return dto;
    }

}
