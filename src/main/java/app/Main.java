package app;

import app.config.HibernateConfig;
import app.daos.impl.InstructorDAO;
import app.daos.impl.SkiLessonDAO;
import app.entities.Instructor;
import app.entities.SkiLesson;
import app.populators.InstructorPopulator;
import app.populators.SecurityPopulators.RolePopulator;
import app.populators.SecurityPopulators.UserPopulator;
import app.populators.SkiLessonPopulator;
import app.rest.ApplicationConfig;
import app.rest.Routes;
import app.security.daos.RoleDAO;
import app.security.daos.UserDAO;
import app.security.entities.Role;
import app.security.entities.User;
import app.security.rest.SecurityRoutes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        setupDatabaseSecurity(emf);
        setupDatbase(emf);

        ApplicationConfig
                .getInstance()
                .initiateServer()
                .securityCheck()
                .setRoute(Routes.getRoutes(emf))
                .setRoute(SecurityRoutes.getRoutes(emf))
                .handleException()
                .startServer(7070); //TODO change this to an available port for deployment

    }

    private static void setupDatabaseSecurity(EntityManagerFactory emf)
    {
        try(EntityManager em = emf.createEntityManager())
        {
            // populates the database with roles
            List<Role> roles = RolePopulator.populate();
            RoleDAO roleDAO = RoleDAO.getInstance(emf);
            roles.forEach(roleDAO::createRole);

            // populates the database with users
            List<User> users = UserPopulator.populate();
            UserDAO userDAO = UserDAO.getInstance(emf);
            users.forEach(userDAO::create);
        }
    }

    private static void setupDatbase(EntityManagerFactory emf)
    {
        try(EntityManager em = emf.createEntityManager())
        {
            // fetches the lessons and instructors from populators
            List<SkiLesson> lessons = SkiLessonPopulator.populate();
            List<Instructor> instructors = InstructorPopulator.populate();

            // persists all the instructors and their lessons
            InstructorDAO instructorDAO = InstructorDAO.getInstance(emf);
            instructors.forEach(instructorDAO::create);

            // persists the last lesson with no instructor
            SkiLessonDAO skiLessonDAO = SkiLessonDAO.getInstance(emf);
            skiLessonDAO.create(lessons.get(3));
        }
    }

}
