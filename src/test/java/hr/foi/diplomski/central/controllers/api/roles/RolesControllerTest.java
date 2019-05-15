package hr.foi.diplomski.central.controllers.api.roles;

import hr.foi.diplomski.central.CentralApplication;
import hr.foi.diplomski.central.controllers.api.roles.data.RolesDto;
import hr.foi.diplomski.central.service.roles.RolesService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static hr.foi.diplomski.central.SecurityUtilsTest.TOKEN;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CentralApplication.class)
public class RolesControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RolesService rolesService;

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
    public void returnAllRolesDto_onGetAllRoles() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka role."),
                fieldWithPath("[].itemName").description("Naziv role."),
        };

        when(rolesService.findAllRoles()).thenReturn(createRoles());

        mvc.perform(get("/api/roles").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("rolesControllerGetAllRoles", responseFields(book)));
    }

    private List<RolesDto> createRoles() {
        RolesDto r1 = new RolesDto();
        r1.setId(1L);
        r1.setItemName("ROLE_ADMIN");


        RolesDto r2 = new RolesDto();
        r2.setId(2L);
        r2.setItemName("ROLE_USER");

        RolesDto r3 = new RolesDto();
        r3.setId(3L);
        r3.setItemName("ROLE_SENSOR");

        return Arrays.asList(r1, r2, r3);
    }

}
