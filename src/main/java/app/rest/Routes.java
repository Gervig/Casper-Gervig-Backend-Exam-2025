package app.rest;

import app.controllers.impl.SkiLessonController;
import app.dtos.ErrorMessage;
import app.dtos.SkiLessonDTO;
import app.enums.Level;
import app.services.SkiLessonService;
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
                        if(lessonDTO == null)
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
                    skiLessonController.populate();
                });
                get("/fetch/{level}", ctx ->
                {
                    Level level = Level.valueOf(ctx.pathParam("{level}"));
                    List<SkiLessonDTO> lessonDTOS = SkiLessonService.getLessons(level);
                    ctx.json(lessonDTOS);
                });
            });
        };
    }
}