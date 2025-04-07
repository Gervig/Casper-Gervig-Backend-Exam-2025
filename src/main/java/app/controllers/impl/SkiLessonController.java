package app.controllers.impl;

import app.controllers.ISkiLesson;
import app.daos.impl.InstructorDAO;
import app.daos.impl.SkiLessonDAO;
import app.dtos.SkiLessonDTO;
import app.entities.SkiLesson;
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
    public List<SkiLessonDTO> getAllLessons()
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
    public SkiLessonDTO getLessonById(Long id)
    {

    }

    @Override
    public SkiLessonDTO createLesson(SkiLessonDTO lesson)
    {
        return null;
    }

    @Override
    public SkiLessonDTO updateLesson(SkiLessonDTO lesson)
    {
        return null;
    }

    @Override
    public void deleteLesson(Long id)
    {

    }

    @Override
    public void addInstructor(Long lessonId, Long instructorId)
    {

    }

    @Override
    public void removeLesson(SkiLessonDTO lesson)
    {

    }
}
