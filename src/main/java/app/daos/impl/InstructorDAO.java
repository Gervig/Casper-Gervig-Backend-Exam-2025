package app.daos.impl;

import app.daos.IDAO;
import app.entities.Instructor;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

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

            Instructor existingInstructor = findByUniqueFields(em, instructor);

            if (existingInstructor != null)
            {
                em.getTransaction().commit();
                return existingInstructor;
            }

            em.persist(instructor);
            em.getTransaction().commit();
            return instructor;

        } catch (Exception e)
        {
            throw new ApiException(401, "Error creating or fetching instructor", e);
        }
    }

    private Instructor findByUniqueFields(EntityManager em, Instructor instructor)
    {
        try
        {
            return em.createQuery(
                            "SELECT i FROM Instructor i WHERE i.firstname = :firstname AND i.lastname = :lastname AND i.email = :email",
                            Instructor.class)
                    .setParameter("firstname", instructor.getFirstname())
                    .setParameter("lastname", instructor.getLastname())
                    .setParameter("email", instructor.getEmail())
                    .getSingleResult();
        } catch (NoResultException e)
        {
            return null;
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
            throw new ApiException(401, "Error updating instructor", e);
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
