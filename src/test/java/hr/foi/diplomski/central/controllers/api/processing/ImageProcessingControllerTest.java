package hr.foi.diplomski.central.controllers.api.processing;

import com.google.gson.Gson;
import hr.foi.diplomski.central.CentralApplication;
import hr.foi.diplomski.central.controllers.api.processing.data.ImageProcessingResult;
import hr.foi.diplomski.central.controllers.api.processing.data.ImageWrapper;
import hr.foi.diplomski.central.service.google.GoogleService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static hr.foi.diplomski.central.SecurityUtilsTest.TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CentralApplication.class)
public class ImageProcessingControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private GoogleService googleService;

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
    @Ignore
    public void returnImageProcessingResult_onProcessImage() throws Exception {
        FieldDescriptor[] book = new FieldDescriptor[]{
                fieldWithPath("type").description("Tip uređaja sa slike."),
                fieldWithPath("par1").description("Prvi parametar."),
                fieldWithPath("par2").description("Drugi parametar."),
                fieldWithPath("par3").description("Treći parametar."),
        };

        when(googleService.processImage(any())).thenReturn(createImageProcessingResult());

        mvc.perform(post("/api/image-processing").header("Authorization", TOKEN)
                .content(new Gson().toJson(createImageWrapper())))
                .andExpect(status().isOk())
                .andDo(document("imageProcessingControllerProcessImage", responseFields(book)));
    }

    private ImageWrapper createImageWrapper() {
        ImageWrapper imageWrapper = new ImageWrapper();
        imageWrapper.setMultipartFile(new MockMultipartFile("image",
                "image.jpg", MediaType.IMAGE_JPEG_VALUE, "ImageBytes".getBytes()));

        return imageWrapper;
    }

    private ImageProcessingResult createImageProcessingResult() {
        ImageProcessingResult result = new ImageProcessingResult();
        result.setType("ibeacon");
        result.setPar1("B5B182C7-EAB1-4988-AA99-B5C1517008D9");
        result.setPar2(1L);
        result.setPar3(2L);

        return result;
    }

}
