package app.rest;

import app.controllers.impl.SkiLessonController;
import app.dtos.ErrorMessage;
import app.dtos.SkiLessonDTO;
import app.enums.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
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

        return () ->
        {
            path("skilessons", () ->
            {
                get("/", ctx ->
                {
                    logger.info("Information about the resource that was accessed: " + ctx.path());
                    List<SkiLessonDTO> lessonDTOS = skiLessonController.getAllLessons();
                    ctx.json(lessonDTOS);
                });
                get("/{id}", ctx ->
                {
                    try
                    {
                        Long id = Long.valueOf(ctx.pathParam("id"));
                        SkiLessonDTO lessonDTO = skiLessonController.getLessonById(id);
                        if (lessonDTO == null)
                        {
                            throw new NullPointerException();
                        }
                        ctx.json(lessonDTO);
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No lesson with that ID");
                        ctx.status(404).json(error);
                    }
                });
                post("/", ctx ->
                {
                    try
                    {
                        SkiLessonDTO incomingLesson = ctx.bodyAsClass(SkiLessonDTO.class);
                        SkiLessonDTO returnedLesson = skiLessonController.createLesson(incomingLesson);

                        //TODO write logic for not creating duplicate lessons

                        ctx.json(returnedLesson);
                    } catch (IllegalStateException ise)
                    {
                        ErrorMessage error = new ErrorMessage("Incorrect JSON");
                        ctx.status(400).json(error);
                    }
                });
                put("/{id}", ctx ->
                {
                    Long id = Long.valueOf(ctx.pathParam("id"));
                    try
                    {
                        // checks if the lesson exists
                        SkiLessonDTO test = skiLessonController.getLessonById(id);
                        if (test == null)
                        {
                            throw new NullPointerException();
                        }
                        SkiLessonDTO incomingLesson = ctx.bodyAsClass(SkiLessonDTO.class);
                        if (incomingLesson.getId() == null)
                        {
                            incomingLesson.setId(id); // in case the body forgot to add the id
                        }
                        SkiLessonDTO returnedLesson = skiLessonController.updateLesson(incomingLesson);
                        ctx.json(returnedLesson);
                    } catch (IllegalStateException ise)
                    {
                        ErrorMessage error = new ErrorMessage("Incorrect JSON");
                        ctx.status(400).json(error);
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No lesson with that ID");
                        ctx.status(404).json(error);
                    }
                });
                delete("/{id}", ctx ->
                {
                    Long id = Long.valueOf(ctx.pathParam("id"));
                    try
                    {
                        // checks if the lesson exists
                        SkiLessonDTO test = skiLessonController.getLessonById(id);
                        if (test == null)
                        {
                            throw new NullPointerException();
                        }
                        skiLessonController.deleteLesson(id);
                        ctx.status(204);
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No lesson with that ID");
                        ctx.status(404).json(error);
                    }
                });
                put("{lessonId}/instructors/{instructorId}", ctx ->
                {
                    Long lessonId = Long.valueOf(ctx.pathParam("lessonId"));
                    Long instructorId = Long.valueOf(ctx.pathParam("instructorId"));
                    try
                    {
                        SkiLessonDTO lessonDTO = skiLessonController.addInstructor(lessonId, instructorId);
                        ctx.json(lessonDTO);
                    } catch (IllegalStateException ise)
                    {
                        ErrorMessage error = new ErrorMessage("Incorrect JSON");
                        ctx.status(400).json(error);
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No lesson or instructor with that ID");
                        ctx.status(404).json(error);
                    }
                });
                post("/populate", ctx ->
                {
                    try
                    {
                        skiLessonController.populate();
                        ctx.json(objectMapper.createObjectNode().put("msg",
                                "Database successfully populated"));
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("Database failed to get populated");
                        ctx.status(500).json(error);
                    }
                });
                get("/fetch/{level}", ctx ->
                {
                    Level level = Level.valueOf(ctx.pathParam("{level}"));
                    List<SkiLessonDTO> lessonDTOS = skiLessonController.fetchFromAPI(level);
                    ctx.json(lessonDTOS);
                });
                get("fetch/duration/{level}", ctx ->
                {
                    Level level = Level.valueOf(ctx.pathParam("{level}"));
                    List<SkiLessonDTO> lessonDTOS = skiLessonController.fetchFromAPI(level);
                    int totalMinutes = 0;
                    for (SkiLessonDTO l : lessonDTOS)
                    {
                        totalMinutes += l.getDurationMinutes();
                    }
                    ctx.json(objectMapper.createObjectNode().put("msg",
                            "Total duration of instructions for ski lessons with level "
                                    + level + " is: " + totalMinutes + " minutes"));
                });
                get("/level/{level}", ctx ->
                {
                    Level level = Level.valueOf(ctx.pathParam("{level}"));
                    List<SkiLessonDTO> skiLessonDTOS = skiLessonController.getByLevel(level);
                    ctx.json(skiLessonDTOS);
                });
                get("/instructors/{instructorId}/sum", ctx ->
                {
                    Long instructorId = Long.valueOf(ctx.pathParam("instructorId"));
                    BigDecimal sum = skiLessonController.sumForInstructor(instructorId);
                    ctx.json(objectMapper.createObjectNode().put("msg",
                            "Total price for lessons for instructor with ID " + instructorId
                                    + " is: " + sum));
                });
            });
        };
    }
}