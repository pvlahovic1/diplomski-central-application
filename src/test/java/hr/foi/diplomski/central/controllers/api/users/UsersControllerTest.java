package hr.foi.diplomski.central.controllers.api.users;

import hr.foi.diplomski.central.CentralApplication;
import hr.foi.diplomski.central.controllers.api.roles.data.RolesDto;
import hr.foi.diplomski.central.controllers.api.users.data.UserDto;
import hr.foi.diplomski.central.service.users.UserService;
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
import java.util.Arrays;
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
public class UsersControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

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
    public void returAllUserDtos_onGetAllUsers() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("[].id").description("Identifikacijska oznaka korisnika."),
                fieldWithPath("[].username").description("Korisničko ime."),
                fieldWithPath("[].password").description("Aplikacija nikada ne vraća lozinku."),
                fieldWithPath("[].firstName").description("Ime korisnika."),
                fieldWithPath("[].lastName").description("Prezime korisnika."),
                fieldWithPath("[].active").description("Definira je li korisnik aktivan."),
                fieldWithPath("[].roles").description("Definira polje rola koje su dodjeljene korisniku."),
                fieldWithPath("[].roles[].id").description("Identifikacijska oznaka role."),
                fieldWithPath("[].roles[].itemName").description("Naziv role."),
        };

        when(userService.findAllUsers()).thenReturn(createUserDtos());

        mvc.perform(get("/api/users").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("usersControllerGetAllUsers", responseFields(book)));

    }

    @Test
    public void returUserDto_onGetUserById() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka korisnika."),
                fieldWithPath("username").description("Korisničko ime."),
                fieldWithPath("password").description("Aplikacija nikada ne vraća lozinku."),
                fieldWithPath("firstName").description("Ime korisnika."),
                fieldWithPath("lastName").description("Prezime korisnika."),
                fieldWithPath("active").description("Definira je li korisnik aktivan."),
                fieldWithPath("roles").description("Definira polje rola koje su dodjeljene korisniku."),
                fieldWithPath("roles[].id").description("Identifikacijska oznaka role."),
                fieldWithPath("roles[].itemName").description("Naziv role."),
        };

        when(userService.findUserById(any())).thenReturn(createUserDto(1));

        mvc.perform(get("/api/users/1").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("usersControllerGetUserById", responseFields(book)));
    }

    @Test
    public void saveUser_onSaveUser() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka korisnika."),
                fieldWithPath("username").description("Korisničko ime."),
                fieldWithPath("password").description("Aplikacija nikada ne vraća lozinku."),
                fieldWithPath("firstName").description("Ime korisnika."),
                fieldWithPath("lastName").description("Prezime korisnika."),
                fieldWithPath("active").description("Definira je li korisnik aktivan."),
                fieldWithPath("roles").description("Definira polje rola koje su dodjeljene korisniku."),
                fieldWithPath("roles[].id").description("Identifikacijska oznaka role."),
                fieldWithPath("roles[].itemName").description("Naziv role."),
        };

        when(userService.saveUser(any())).thenReturn(createUserDto(1));

        mvc.perform(post("/api/users").header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"id\":null,\"username\":\"username1\",\"password\":\"password\",\"firstName\":\"FirstName1\"," +
                        "\"lastName\":\"LastName1\",\"roles\":[{\"id\":1},{\"id\":2}],\"active\":false}").getBytes()))
                .andExpect(status().isOk())
                .andDo(document("usersControllerSaveUser", responseFields(book)));
    }

    @Test
    public void activateUser_onActivateUser() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka korisnika."),
                fieldWithPath("username").description("Korisničko ime."),
                fieldWithPath("password").description("Aplikacija nikada ne vraća lozinku."),
                fieldWithPath("firstName").description("Ime korisnika."),
                fieldWithPath("lastName").description("Prezime korisnika."),
                fieldWithPath("active").description("Definira je li korisnik aktivan."),
                fieldWithPath("roles").description("Definira polje rola koje su dodjeljene korisniku."),
                fieldWithPath("roles[].id").description("Identifikacijska oznaka role."),
                fieldWithPath("roles[].itemName").description("Naziv role."),
        };

        UserDto user = createUserDto(1);
        user.setActive(true);

        when(userService.activateUser(any())).thenReturn(user);

        mvc.perform(put("/api/users/1/activate").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("usersControllerActivateUser", responseFields(book)));
    }

    @Test
    public void activateUser_onDeactivateUser() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("id").description("Identifikacijska oznaka korisnika."),
                fieldWithPath("username").description("Korisničko ime."),
                fieldWithPath("password").description("Aplikacija nikada ne vraća lozinku."),
                fieldWithPath("firstName").description("Ime korisnika."),
                fieldWithPath("lastName").description("Prezime korisnika."),
                fieldWithPath("active").description("Definira je li korisnik aktivan."),
                fieldWithPath("roles").description("Definira polje rola koje su dodjeljene korisniku."),
                fieldWithPath("roles[].id").description("Identifikacijska oznaka role."),
                fieldWithPath("roles[].itemName").description("Naziv role."),
        };

        UserDto user = createUserDto(1);

        when(userService.deactivateUser(any())).thenReturn(user);

        mvc.perform(put("/api/users/1/deactivate").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("usersControllerDeactivateUser", responseFields(book)));
    }

    @Test
    public void deleteUser_onDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(any());

        mvc.perform(delete("/api/users/1").header("Authorization", TOKEN))
                .andExpect(status().isOk())
                .andDo(document("usersControllerDeleteUser"));
    }

    private List<UserDto> createUserDtos() {
        List<UserDto> dtos = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            dtos.add(createUserDto(i + 1));
        }

        return dtos;
    }

    private UserDto createUserDto(int index) {
        UserDto userDto = new UserDto();
        userDto.setId((long) index);
        userDto.setUsername(String.format("username%s", index));
        userDto.setFirstName(String.format("FirstName%s", index));
        userDto.setLastName(String.format("LastName%s", index));
        userDto.setRoles(createRoles());
        userDto.setActive(index % 2 == 0);

        return userDto;
    }

    private List<RolesDto> createRoles() {
        RolesDto r1 = new RolesDto();
        r1.setId(1L);
        r1.setItemName("ROLE_ADMIN");


        RolesDto r2 = new RolesDto();
        r2.setId(2L);
        r2.setItemName("ROLE_USER");

        return Arrays.asList(r1, r2);
    }

}
