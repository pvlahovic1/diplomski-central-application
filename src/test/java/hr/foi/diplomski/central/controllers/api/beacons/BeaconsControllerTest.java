package hr.foi.diplomski.central.controllers.api.beacons;

import com.google.gson.Gson;
import hr.foi.diplomski.central.CentralApplication;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;
import hr.foi.diplomski.central.service.beacon.BeaconService;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
public class BeaconsControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BeaconService beaconService;

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
    public void returnBeaconDto_onGetAllBeacons() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka beacona."),
                fieldWithPath("[].uuid").description("UUID oznaka beacona."),
                fieldWithPath("[].major").description("Major oznaka beacona."),
                fieldWithPath("[].minor").description("Minor oznaka beacona."),
                fieldWithPath("[].devices").description("Polje svih uređaja koje beacon predstavlja."),
                fieldWithPath("[].devices.[].id").description("Identifikacijska oznaka uređaja kojeg beacon predstavlja."),
                fieldWithPath("[].devices.[].name").description("Naziv uređaja kojeg beacon predstavlja."),
                fieldWithPath("[].devices.[].beaconData").description("Podaci o beaconu koji se nalazi na trenutnom uređaju."),
        };

        when(beaconService.findAllBeacons()).thenReturn(createBeaconDtos());

        mvc.perform(get("/api/beacons").header("Authorization", TOKEN)).andExpect(status().isOk())
                .andDo(document("beaconsControllerGetAllBeacons", responseFields(book)));
    }

    @Test
    public void returnBeaconDto_onGetBeaconById() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka beacona."),
                fieldWithPath("uuid").description("UUID oznaka beacona."),
                fieldWithPath("major").description("Major oznaka beacona."),
                fieldWithPath("minor").description("Minor oznaka beacona."),
                fieldWithPath("devices").description("Polje svih uređaja koje beacon predstavlja."),
                fieldWithPath("devices.[].id").description("Identifikacijska oznaka uređaja kojeg beacon predstavlja."),
                fieldWithPath("devices.[].name").description("Naziv uređaja kojeg beacon predstavlja."),
                fieldWithPath("devices.[].beaconData").description("Podaci o beaconu koji se nalazi na trenutnom uređaju."),
        };

        when(beaconService.findBeaconById(any())).thenReturn(createBeaconDto(1));

        mvc.perform(get("/api/beacons/1").header("Authorization", TOKEN)).andExpect(status().isOk())
                .andDo(document("beaconsControllerGetBeaconById", responseFields(book)));
    }

    @Test
    public void returnBeaconDto_onGetAllFreeBeacons() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka beacona."),
                fieldWithPath("[].itemName").description("Podaci o trenutno beaconu."),
        };

        List<BeaconDto> dtos = createBeaconDtos();

        for (BeaconDto dto : dtos) {
            dto.setDevices(Collections.emptyList());
        }

        when(beaconService.findAllFreeBeacons()).thenReturn(createBeaconViewDtos());

        mvc.perform(get("/api/beacons/free").header("Authorization", TOKEN)).andExpect(status().isOk())
                .andDo(document("beaconsControllerGetAllFreeBeacons", responseFields(book)));
    }

    @Test
    public void returnBeaconDtos_onSaveBeacon() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka beacona."),
                fieldWithPath("uuid").description("UUID oznaka beacona."),
                fieldWithPath("major").description("Major oznaka beacona."),
                fieldWithPath("minor").description("Minor oznaka beacona."),
                fieldWithPath("devices").description("Polje svih uređaja koje beacon predstavlja."),
                fieldWithPath("devices.[].id").description("Identifikacijska oznaka uređaja kojeg beacon predstavlja."),
                fieldWithPath("devices.[].name").description("Naziv uređaja kojeg beacon predstavlja."),
                fieldWithPath("devices.[].beaconData").description("Podaci o beaconu koji se nalazi na trenutnom uređaju."),
        };

        BeaconDto dto = createBeaconDto(1);
        dto.setId(null);

        when(beaconService.saveBeacon(any())).thenReturn(createBeaconDto(1));

        mvc.perform(post("/api/beacons").header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(dto))).andExpect(status().isOk())
                .andDo(document("beaconsControllerGetBeaconById", responseFields(book)));
    }


    @Test
    public void deleteBeacon_onDeleteBeacon() throws Exception {
        doNothing().when(beaconService).deleteBeaon(any());

        mvc.perform(delete("/api/beacons/1").header("Authorization", TOKEN)).andExpect(status().isOk())
                .andDo(document("beaconsControllerDeleteBeacon"));
    }

    private List<BeaconDto> createBeaconDtos() {
        List<BeaconDto> dtos = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            dtos.add(createBeaconDto(i + 1));
        }

        return dtos;
    }

    private List<BeaconViewDto> createBeaconViewDtos() {
        List<BeaconViewDto> dtos = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            dtos.add(createBeaconViewDto(i + 1));
        }

        return dtos;
    }

    private BeaconViewDto createBeaconViewDto(int index) {
        BeaconViewDto dto = new BeaconViewDto();
        dto.setId((long) index);
        dto.setItemName(String.format("B5B182C7-EAB1-4988-AA99-B5C1517008D9 1 %s", index));

        return dto;
    }

    private BeaconDto createBeaconDto(int index) {
        BeaconDto dto = new BeaconDto();
        dto.setId((long) index);
        dto.setUuid("B5B182C7-EAB1-4988-AA99-B5C1517008D9");
        dto.setMajor(1);
        dto.setMinor(index);
        dto.setDevices(Collections.singletonList(createDeviceViewDto(index)));

        return dto;
    }

    private DeviceViewDto createDeviceViewDto(int index) {
        DeviceViewDto dto = new DeviceViewDto();
        dto.setId((long) index);
        dto.setBeaconData(String.format("B5B182C7-EAB1-4988-AA99-B5C1517008D9 1 %s", index));
        dto.setName(String.format("Device %s", index));

        return dto;
    }

}
