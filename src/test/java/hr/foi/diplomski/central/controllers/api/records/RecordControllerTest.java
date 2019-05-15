package hr.foi.diplomski.central.controllers.api.records;


import hr.foi.diplomski.central.CentralApplication;
import hr.foi.diplomski.central.service.records.RecordService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static hr.foi.diplomski.central.SecurityUtilsTest.TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CentralApplication.class)
public class RecordControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RecordService recordService;

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
    public void doSave_onSaveNewDeviceRecord() throws Exception {
        doNothing().when(recordService).createNewRecord(any());

        mvc.perform(post("/api/records").header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceId\":\"2bc0bd3d00ef18d02398d4e4b2019857e1682102607c4dd5fbf8912fada254ee\",\"sensorRecords\":[{\"uuid\":\"B5B182C7-EAB1-4988-AA99-B5C1517008D9\", \"major\":1,\"minor\":2,\"distance\":3},{\"uuid\":\"B5B182C7-EAB1-4988-AA99-B5C1517008D9\", \"major\":1,\"minor\":3,\"distance\":2}]}".getBytes()))
                .andExpect(status().isOk())
                .andDo(document("recordControllerSaveNewDeviceRecord"));
    }

}
