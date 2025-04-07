package app.controllers.impl;

import app.controllers.ISkiLesson;
import app.daos.impl.InstructorDAO;
import app.daos.impl.SkiLessonDAO;
import app.dtos.SkiLessonDTO;
import app.entities.Instructor;
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
        Instructor instructor = lesson.getInstructor();

        // checks if the lesson has an instructor and if it's already been created
        if(instructor != null && instructor.getId() == null)
        {
            // creates the instructor and the lesson
            instructor = instructorDAO.create(instructor);
        }

        // checks if the lesson has already been created
        if(lessonDTO.getId() == null && instructor == null)
        {
            lesson = skiLessonDAO.create(lesson);
        }

        // creates a lesson DTO from the lesson entity
        SkiLessonDTO newLessonDTO = new SkiLessonDTO(lesson, true);
        return newLessonDTO;
    }

    @Override
    public SkiLessonDTO updateLesson(SkiLessonDTO lessonDTO) throws ApiException
    {
        SkiLesson existingLesson = skiLessonDAO.read(lessonDTO.getId());
        if(existingLesson == null)
        {
            throw new ApiException(404, "Lesson not found");
        }

        //TODO figure out a cleaner way to do this

        // ensures no null values
        if(lessonDTO.getStarttime() != null) existingLesson.setStarttime(lessonDTO.getStarttime());
        if(lessonDTO.getEndtime() != null) existingLesson.setEndtime(lessonDTO.getEndtime());
        if(lessonDTO.getLongitude() != 0.0d) existingLesson.setLongitude(lessonDTO.getLongitude());
        if(lessonDTO.getLatitude() != 0.0d) existingLesson.setLatitude(lessonDTO.getLatitude());
        if(lessonDTO.getName() != null) existingLesson.setName(lessonDTO.getName());
        if(lessonDTO.getPrice() != null) existingLesson.setPrice(lessonDTO.getPrice());
        if(lessonDTO.getLevel() != null) existingLesson.setLevel(lessonDTO.getLevel());
        if(lessonDTO.getInstructor() != null)
        {
            // tries to find the instructor
            Instructor existingInstructor = instructorDAO.read(lessonDTO.getInstructor().getId());
            if(existingInstructor == null)
            {
                // if it's a new instructor, creates them
                Instructor newInstructor = lessonDTO.getInstructor().toEntity();
                newInstructor = instructorDAO.create(newInstructor);
                // sets newly persisted instructor to lesson
                existingLesson.setInstructor(newInstructor);
            } else
            {
                // if instructor already exists, sets them to the lesson
                existingLesson.setInstructor(existingInstructor);
            }
        }

        // updates the lesson
        SkiLesson updatedLesson = skiLessonDAO.update(existingLesson);
        SkiLessonDTO updatedLessonDTO = new SkiLessonDTO(updatedLesson, true);
        return updatedLessonDTO;
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
