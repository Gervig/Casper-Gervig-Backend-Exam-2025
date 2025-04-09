package app.daos.impl;

import app.config.HibernateConfig;
import app.entities.Instructor;
import app.entities.Location;
import app.entities.SkiLesson;
import app.populators.InstructorPopulator;
import app.populators.LocationPopulator;
import app.populators.SkiLessonPopulator;
import app.security.daos.UserDAO;
import app.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkiLessonDAOTest
{
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final SkiLessonDAO skiLessonDAO = SkiLessonDAO.getInstance(emf);
    private static final InstructorDAO instructorDAO = InstructorDAO.getInstance(emf);
    private static final LocationDAO locationDAO = LocationDAO.getInstance(emf);
    private List<SkiLesson> lessons;
    private List<Instructor> instructors;
    private List<Location> locations;

    @BeforeEach
    void setup()
    {
        instructors = InstructorPopulator.populate();
        locations = LocationPopulator.populate();
        lessons = SkiLessonPopulator.populate(instructors, locations);

        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            // Persist all lessons (cascades location & instructor)
            lessons.forEach(skiLessonDAO::create);

            // Persists the instructor with no lesson
            instructorDAO.create(instructors.get(2));

            em.getTransaction().commit();
        }
    }

    @AfterEach
    void tearDown()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            // clears previous data
            em.createQuery("DELETE FROM SkiLesson").executeUpdate();
            em.createQuery("DELETE FROM Instructor").executeUpdate();
            em.createQuery("DELETE FROM Location").executeUpdate();

            // Reset ID sequences (for PostgresSQL & databases that support sequences)
            em.createNativeQuery("ALTER SEQUENCE skilesson_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE instructor_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE location_id_seq RESTART WITH 1").executeUpdate();

            em.getTransaction().commit();
        }
    }


    @Test
    void getInstance()
    {
        SkiLessonDAO instanceTest = SkiLessonDAO.getInstance(emf);
        assertNotNull(instanceTest);
        assertEquals(instanceTest, skiLessonDAO);
    }

    @Test
    void create()
    {
        SkiLesson expected = getTestLesson();
        SkiLesson actual = skiLessonDAO.create(expected);

        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void read()
    {
        String expected = lessons.get(0).getName();
        String actual = skiLessonDAO.read(lessons.get(0).getId()).getName();

        assertEquals(expected, actual);
    }

    @Test
    void readAll()
    {
        List<SkiLesson> expected = skiLessonDAO.readAll();

        assertEquals(expected.size(), 4);
    }

    @Test
    void update()
    {
        SkiLesson expected = lessons.get(0);

        expected.setPrice(BigDecimal.valueOf(500.00));

        expected = skiLessonDAO.update(expected);

        assertEquals(expected.getPrice(), BigDecimal.valueOf(500.00));
    }

    @Test
    void delete()
    {
        List<SkiLesson> lessonListBefore = skiLessonDAO.readAll();

        assertEquals(lessonListBefore.size(), 4);

        skiLessonDAO.delete(lessons.get(0).getId());

        List<SkiLesson> lessonListAfter = skiLessonDAO.readAll();

        assertEquals(lessonListAfter.size(), 3);
    }

    @Test
    void addInstructorToSkiLesson()
    {
        // the lesson with no instructor
        SkiLesson lesson = lessons.get(3);
        // the instructor with no lesson (could have used any)
        Instructor instructor = instructors.get(2);

        // asserts that it has no instructor
        assertNull(lesson.getInstructor());
        // asserts that the instructor has no lessons
        assertTrue(instructor.getLessons().isEmpty());

        lesson = skiLessonDAO.addInstructorToSkiLesson(lesson.getId(), instructor.getId());

        // asserts that the instructor was added to the lesson
        assertEquals(instructor.getId(), lesson.getInstructor().getId());
        // asserts that the instructor now has a lesson
        assertEquals(lesson.getInstructor().getLessons().size(), 1);
    }

    @Test
    void getSkiLessonsByInstructor()
    {
        // reads how many lessons the instructor actually has in the database
        int actual = skiLessonDAO.getSkiLessonsByInstructor(instructors.get(0).getId()).size();

        assertEquals(2, actual);
    }

    // Helper method to create a test lesson
    private SkiLesson getTestLesson()
    {
        SkiLesson lesson = SkiLesson.builder()
                .name("Test lesson")
                .build();

        return lesson;
    }
}