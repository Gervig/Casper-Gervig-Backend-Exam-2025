package app.controllers.impl;

import app.controllers.ISkiLesson;
import app.daos.impl.InstructorDAO;
import app.daos.impl.SkiLessonDAO;
import app.dtos.SkiLessonDTO;
import app.entities.SkiLesson;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SkiLessonController implements ISkiLesson<SkiLessonDTO, Long>
{
    private static EntityManagerFactory emf;
    private SkiLessonDAO skiLessonDAO;
    private InstructorDAO instructorDAO;

    // instantiates the DAOs
    public SkiLessonController(EntityManagerFactory _emf)
    {
        if(emf == null)
        {
            emf = _emf;
        }
        this.skiLessonDAO = SkiLessonDAO.getInstance(emf);
        this.instructorDAO = InstructorDAO.getInstance(emf);
    }

    @Override
    public List<SkiLessonDTO> getAllLessons() throws ApiException
    {
        // reads all the lessons from the database
        List<SkiLesson> lessons = skiLessonDAO.readAll();
        // maps all the lessons to DTOs
        List<SkiLessonDTO> lessonDTOS = lessons.stream()
                .map(lesson -> new SkiLessonDTO(lesson, true))
                .toList();
        return lessonDTOS;
    }

    @Override
    public SkiLessonDTO getLessonById(Long id) throws ApiException
    {
        SkiLesson lesson = skiLessonDAO.read(id);
        SkiLessonDTO skiLessonDTO = new SkiLessonDTO(lesson, true);
        return skiLessonDTO;
    }

    @Override
    public SkiLessonDTO createLesson(SkiLessonDTO lessonDTO) throws ApiException
    {
        SkiLesson lesson = lessonDTO.toEntity();
    }

    @Override
    public SkiLessonDTO updateLesson(SkiLessonDTO lesson) throws ApiException
    {
        return null;
    }

    @Override
    public void deleteLesson(Long id) throws ApiException
    {

    }

    @Override
    public void addInstructor(Long lessonId, Long instructorId) throws ApiException
    {

    }

    @Override
    public void removeLesson(SkiLessonDTO lesson) throws ApiException
    {

    }
}
