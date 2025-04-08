package app.daos.impl;

import app.daos.IDAO;
import app.entities.Instructor;
import app.entities.Location;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class LocationDAO implements IDAO<Location, Long>
{
    private static EntityManagerFactory emf;
    private static LocationDAO instance;

    public static LocationDAO getInstance(EntityManagerFactory _emf)
    {
        if (instance == null)
        {
            instance = new LocationDAO();
        }
        emf = _emf;
        return instance;
    }


    @Override
    public Location create(Location location)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
            return location;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error creating location", e);
        }
    }

    @Override
    public Location read(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.find(Location.class, id);
        } catch (Exception e)
        {
            throw new ApiException(404, "Error could not find location", e);
        }
    }

    @Override
    public List<Location> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT l FROM Location l", Location.class).getResultList();
        } catch (Exception e)
        {
            throw new ApiException(404, "Error finding list of locations", e);
        }
    }

    @Override
    public Location update(Location location)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Location updatedLocation = em.merge(location);
            em.getTransaction().commit();
            return updatedLocation;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error updating location", e);
        }
    }

    @Override
    public void delete(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Location location = em.find(Location.class, id);
            if (location == null)
            {
                em.getTransaction().rollback();
                throw new NullPointerException();
            }
            em.remove(location);
            em.getTransaction().commit();
        } catch (Exception e)
        {
            throw new ApiException(401, "Error removing location", e);
        }
    }
}
