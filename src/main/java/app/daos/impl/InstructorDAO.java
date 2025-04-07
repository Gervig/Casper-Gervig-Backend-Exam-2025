package app.daos.impl;

import app.daos.IDAO;
import app.entities.Instructor;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class InstructorDAO implements IDAO<Instructor, Long>
{
    private static EntityManagerFactory emf;
    private static InstructorDAO instance;

    public static InstructorDAO getInstance(EntityManagerFactory _emf)
    {
        if (instance == null)
        {
            instance = new InstructorDAO();
        }
        emf = _emf;
        return instance;
    }

    @Override
    public Instructor create(Instructor instructor)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(instructor);
            em.getTransaction().commit();
            return instructor;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error creating instructor", e);
        }
    }

    @Override
    public Instructor read(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.find(Instructor.class, id);
        } catch (Exception e)
        {
            throw new ApiException(404, "Error could not find instructor", e);
        }
    }

    @Override
    public List<Instructor> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT i FROM Instructor i", Instructor.class).getResultList();
        } catch (Exception e)
        {
            throw new ApiException(404, "Error finding list of instructors", e);
        }
    }

    @Override
    public Instructor update(Instructor instructor)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Instructor updatedInstructor = em.merge(instructor);
            em.getTransaction().commit();
            return updatedInstructor;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error updating guide", e);
        }
    }

    @Override
    public void delete(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Instructor instructor = em.find(Instructor.class, id);
            if (instructor == null)
            {
                em.getTransaction().rollback();
                throw new NullPointerException();
            }
            em.remove(instructor);
            em.getTransaction().commit();
        } catch (Exception e)
        {
            throw new ApiException(401, "Error removing instructor", e);
        }
    }
}
