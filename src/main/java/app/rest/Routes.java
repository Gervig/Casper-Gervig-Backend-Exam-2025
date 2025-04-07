package app.rest;

import app.controllers.impl.SkiLessonController;
import app.dtos.ErrorMessage;
import app.dtos.SkiLessonDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Routes
{
    // declare static controllers here
    private static Logger logger = LoggerFactory.getLogger(Routes.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static EndpointGroup getRoutes(EntityManagerFactory emf)
    {
        SkiLessonController skiLessonController = new SkiLessonController(emf);

        return () -> {
            path("skilessons", () -> //
            {
                get("/",ctx -> { // write get path here
                    logger.info("Information about the resource that was accessed: " + ctx.path());
                    List<SkiLessonDTO> lessonDTOS = skiLessonController.getAllLessons();
                    ctx.json(lessonDTOS);
                });
                // write other http methods here
            });
        };
    }
}