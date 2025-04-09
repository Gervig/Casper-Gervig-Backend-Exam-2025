package app.daos.impl;

import app.config.HibernateConfig;
import app.entities.Instructor;
import app.entities.Location;
import app.entities.SkiLesson;
import app.populators.InstructorPopulator;
import app.populators.LocationPopulator;
import app.populators.SkiLessonPopulator;
import app.security.daos.UserDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void read()
    {
    }

    @Test
    void readAll()
    {
    }

    @Test
    void update()
    {
    }

    @Test
    void delete()
    {
    }

    @Test
    void addInstructorToSkiLesson()
    {
    }

    @Test
    void getSkiLessonsByInstructor()
    {
    }
}