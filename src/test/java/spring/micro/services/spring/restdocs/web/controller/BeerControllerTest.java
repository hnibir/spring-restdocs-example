package spring.micro.services.spring.restdocs.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;
import spring.micro.services.spring.restdocs.services.BeerService;
import spring.micro.services.spring.restdocs.web.model.BeerDto;
import spring.micro.services.spring.restdocs.web.model.BeerStyleEnum;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.springframework.org", uriPort = 8082)
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = {"spring.micro.services.spring.restdocs.web.mappers"})
class BeerControllerTest {
    @MockBean
    BeerService beerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private BeerDto validBeerDto;

    @BeforeEach
    void setUp() {
        validBeerDto = BeerDto.builder()
                .id(UUID.randomUUID())
                .name("My Alchohol Free Beer")
                .beerStyle(BeerStyleEnum.PILSNER)
                .price(new BigDecimal("5.00"))
                .upc(12345L)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBeerById() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(validBeerDto);

        mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
                .param("iscold", "yes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(validBeerDto.getId().toString())))
                .andExpect(jsonPath("$.name", is(validBeerDto.getName())))
                .andDo(document("v1/beer-get",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of desired beer to get")
                        ),
                        requestParameters(
                                parameterWithName("iscold").description("Is Beer Cold Query param")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of Beer").type(UUID.class),
                                fieldWithPath("version").description("Version number").type(Integer.class),
                                fieldWithPath("createdDate").description("Date Created").type(OffsetDateTime.class),
                                fieldWithPath("lastModifiedDate").description("Date Updated").type(OffsetDateTime.class),
                                fieldWithPath("name").description("Beer Name").type(String.class),
                                fieldWithPath("beerStyle").description("Beer Style").type(Enum.class),
                                fieldWithPath("upc").description("UPC of Beer").type(Long.class),
                                fieldWithPath("price").description("Price").type(BigDecimal.class),
                                fieldWithPath("quantityOnHand").description("Quantity On hand").type(Integer.class)
                        )
                ));
    }

    @Test
    void saveNewBeer() throws Exception {
        BeerDto beerDto = validBeerDto;
        beerDto.setId(null);

        BeerDto savedDto = BeerDto.builder().id(UUID.randomUUID()).name("Pilz Beer").build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerService.createNewBeer(any())).willReturn(savedDto);

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        mockMvc.perform(post("/api/v1/beer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated())
                .andDo(document("v1/beer-new",
                        requestFields(
                                fields.withPath("id").ignored().type(UUID.class),
                                fields.withPath("version").ignored().type(Integer.class),
                                fields.withPath("createdDate").ignored().type(OffsetDateTime.class),
                                fields.withPath("lastModifiedDate").ignored().type(OffsetDateTime.class),
                                fields.withPath("name").description("Name of the beer").type(String.class),
                                fields.withPath("beerStyle").description("Style of Beer").type(Enum.class),
                                fields.withPath("upc").description("Beer UPC").attributes().type(Long.class),
                                fields.withPath("price").description("Beer price").type(BigDecimal.class),
                                fields.withPath("quantityOnHand").ignored().type(Integer.class)
                        )));

    }

    @Test
    void updateBeerById() throws Exception {
        //given
        BeerDto beerDto = validBeerDto;
        beerDto.setId(null);
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        //when
        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isNoContent());

        then(beerService).should().updateBeer(any(), any());
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}