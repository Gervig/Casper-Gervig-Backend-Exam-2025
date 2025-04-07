package app.controllers.impl;

import app.config.HibernateConfig;
import app.daos.impl.InstructorDAO;
import app.daos.impl.SkiLessonDAO;
import app.dtos.SkiLessonDTO;
import app.enums.Level;
import app.rest.ApplicationConfig;
import app.rest.Routes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/*
RUN THE TESTS ONE BY ONE, THEY FAIL OTHERWISE
 */

//TODO fix test so they work when run all at once
class SkiLessonControllerTest
{
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final SkiLessonController skiLessonController = new SkiLessonController(emf);
    private static final String contentType = "application/json";
    @BeforeEach
    void setup()
    {
        try(EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            // clears database
            em.createQuery("DELETE FROM SkiLesson").executeUpdate();
            em.createQuery("DELETE FROM Instructor").executeUpdate();
            // resets ID sequences to 1
            em.createNativeQuery("ALTER SEQUENCE skilesson_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE instructor_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();

            // populates the databse
            skiLessonController.populate();

        } catch (Exception e)
        {
            e.printStackTrace();
        }


        ApplicationConfig.getInstance()
                .initiateServer()
                .setRoute(Routes.getRoutes(emf))
                .startServer(7777);

        RestAssured.baseURI = "http://localhost:7777/api";
    }

    @Test
    @DisplayName("Test getting all lessons")
    void getAllItems()
    {
        // tests that the endpoint gives status code 200 and the array in the JSON has 4 elements
        List<SkiLessonDTO> lessonDTOS = given()
                .when()
                .get("/skilessons")
                .then()
                .statusCode(200)
                .body("size()", is(4))
                .log().all()
                .extract()
                .as(new TypeRef<List<SkiLessonDTO>>()
                {
                });

        // double checks that the list has all 4 lessons
        assertThat(lessonDTOS.size(), is(4));
    }

    @Test
    @DisplayName("Test getting lesson with ID 1")
    void getLessonById()
    {
        given()
                .when()
                .get("/skilessons/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    @DisplayName("Test creating a new Lesson")
    void createItem()
    {
        SkiLessonDTO lessonDTO = getTestLesson();

        try
        {
            String json = objectMapper.writeValueAsString(lessonDTO);
            given()
                    .when()
                    .body(json)
                    .contentType(contentType)
                    .accept(contentType)
                    .post("/skilessons")
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Park City, Utah, USA"));
        } catch (JsonProcessingException jpe)
        {
            jpe.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Test updating a lesson")
    void updateItem()
    {
        SkiLessonDTO lessonDTO = getTestLesson();
        try
        {
            String json = objectMapper.writeValueAsString(lessonDTO);

            given()
                    .when()
                    .body(json)
                    .contentType(contentType)
                    .accept(contentType)
                    .put("skilessons/1")
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Park City, Utah, USA"));
        } catch (JsonProcessingException jpe)
        {
            jpe.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Test deleting a lesson")
    void deleteLesson()
    {
        // deletes lesson with id 1, test if it gets a 204 code (success)
        when()
                .delete("skilessons/1")
                .then()
                .statusCode(204);

        // tries to get the deleted lesson, should give 404 not found
        when()
                .get("skilessons/1")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Test assigning an instructor to a lesson")
    void addItemToStudent()
    {
        // assigns instructor with ID 1 to lesson with ID 4
        // and checks if the firstname of the instructor is the newly assigned one
        given()
                .when()
                .contentType(contentType)
                .accept(contentType)
                .put("skilessons/4/instructors/1")
                .then()
                .statusCode(200)
                .body("instructor.firstname", equalTo("Jens"));

    }

    // helper methods to create test DTOs
    private SkiLessonDTO getTestLesson()
    {
        SkiLessonDTO lessonDTO = SkiLessonDTO.builder()
                .starttime(null) //TODO fix jackson can't handle LocalDateTime for some reason
                .endtime(null) //TODO same here
                .longitude(111.4980)
                .latitude(40.6461)
                .name("Park City, Utah, USA")
                .price(BigDecimal.valueOf(764.95))
                .level(Level.BEGINNER)
                .instructor(null)
                .build();
        return lessonDTO;
    }
}