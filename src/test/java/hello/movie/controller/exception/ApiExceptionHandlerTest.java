package hello.movie.controller.exception;

import hello.movie.config.SecurityConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class ApiExceptionHandlerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    private RequestSpecification givenAuth() {
        return RestAssured.given()
                .auth().preemptive()
                .basic("user", "userPass");
    }

//    @Test
//    public void whenMethodArgumentMismatch_thenBadRequest() {
//        Response response = givenAuth().get(URL_PREFIX + "/api/movies/dkdkdk");
//        ApiError error = response.as(ApiError.class);
//
//        assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
//        assertEquals(1, error.getErrors().size());
//        assertTrue(error.getErrors().get(0).contains("should be of type"));
//    }



}