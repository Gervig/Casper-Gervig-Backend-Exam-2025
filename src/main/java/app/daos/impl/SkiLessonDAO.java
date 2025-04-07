package app.daos.impl;

import app.daos.IDAO;
import app.daos.ISkiLessonInstructorDAO;
import app.entities.Instructor;
import app.entities.SkiLesson;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;

public class SkiLessonDAO implements IDAO<SkiLesson, Long>, ISkiLessonInstructorDAO<Long>
{
    private static EntityManagerFactory emf;
    private static SkiLessonDAO instance;
    private static InstructorDAO instructorDAO = InstructorDAO.getInstance(emf);

    // singleton
    public static SkiLessonDAO getInstance(EntityManagerFactory _emf)
    {
        if (instance == null)
        {
            instance = new SkiLessonDAO();
        }
        emf = _emf;
        return instance;
    }

    @Override
    public SkiLesson create(SkiLesson skiLesson)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(skiLesson);
            em.getTransaction().commit();
            return skiLesson;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error creating Ski lesson", e);
        }
    }

    @Override
    public SkiLesson read(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.find(SkiLesson.class, id);
        } catch (Exception e)
        {
            throw new ApiException(404, "Error could not find Ski lesson", e);
        }
    }

    @Override
    public List<SkiLesson> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT s FROM SkiLesson s", SkiLesson.class).getResultList();
        } catch (Exception e)
        {
            throw new ApiException(404, "Error finding list of Ski lessons", e);
        }
    }

    @Override
    public SkiLesson update(SkiLesson skiLesson)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            SkiLesson updatedSkiLesson = em.merge(skiLesson);
            em.getTransaction().commit();
            return updatedSkiLesson;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error updating Ski lesson", e);
        }
    }

    @Override
    public void delete(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            SkiLesson skiLesson = em.find(SkiLesson.class, id);
            if (skiLesson == null)
            {
                em.getTransaction().rollback();
                throw new NullPointerException();
            }
            em.remove(skiLesson);
            em.getTransaction().commit();
        } catch (Exception e)
        {
            throw new ApiException(401, "Error removing Ski lesson", e);
        }
    }

    @Override
    public void addInstructorToSkiLesson(Long lessonId, Long instructorId)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            // ensures that the emf is instantiated
            SkiLessonDAO skiLessonDAO = getInstance(emf);

            // finds instructor and lesson
            Instructor instructor = em.find(Instructor.class, instructorId);
            SkiLesson skiLesson = em.find(SkiLesson.class, lessonId);

            // checks if instructor and lesson was found
            if (instructor == null || skiLesson == null)
            {
                throw new NullPointerException();
            }

            // adds lesson to instructor
            instructor.addLesson(skiLesson);
            // sets instructor to lesson, this will overwrite existing instructors!
            skiLesson.setInstructor(instructor);

            // updates the lesson and instructor
            skiLessonDAO.update(skiLesson);
            instructorDAO.update(instructor);
        } catch (NullPointerException npe)
        {
            throw new ApiException(404, "Could not find instructor or Ski lesson", npe);
        } catch (Exception e)
        {
            throw new ApiException(401, "Error adding instructor to instructor to Ski lesson", e);
        }
    }

    @Override
    public Set<SkiLesson> getSkiLessonsByInstructor(Long instructorId)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            Instructor instructor = em.find(Instructor.class, instructorId);

            if(instructor == null)
            {
                throw new NullPointerException();
            }

            Set<SkiLesson> lessons = instructor.getLessons();

            return lessons;

        } catch (NullPointerException npe)
        {
            throw new ApiException(404, "Error could not find instructor", npe);
        }
        catch (Exception e)
        {
            throw new ApiException(401, "Error finding lessons for instructor", e);
        }
    }
}
