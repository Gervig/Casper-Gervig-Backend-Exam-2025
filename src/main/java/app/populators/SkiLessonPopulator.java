package app.populators;

import app.entities.Instructor;
import app.entities.Location;
import app.entities.SkiLesson;
import app.enums.Level;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SkiLessonPopulator
{
    public static List<SkiLesson> populate()
    {
        List<SkiLesson> lessons = new ArrayList<>();
        List<Instructor> instructors = InstructorPopulator.populate();
        List<Location> locations = LocationPopulator.populate();

        SkiLesson s1 = SkiLesson.builder()
                .starttime(LocalDateTime.now())
                .endtime(LocalDateTime.now().plusHours(4))
                .durationMinutes(240)
                .name("Whistler Blackcomb, Canada")
                .price(BigDecimal.valueOf(500.00))
                .level(Level.BEGINNER)
                .build();

        instructors.get(0).getLessons().add(s1);
        s1.setInstructor(instructors.get(0));

        locations.get(0).getLessons().add(s1);
        s1.setLocation(locations.get(0));

        lessons.add(s1);

        SkiLesson s2 = SkiLesson.builder()
                .starttime(LocalDateTime.now())
                .endtime(LocalDateTime.now().plusHours(2))
                .durationMinutes(120)
                .name("Chamonix, France")
                .price(BigDecimal.valueOf(750.00))
                .level(Level.INTERMEDIATE)
                .build();

        instructors.get(0).getLessons().add(s2);
        s2.setInstructor(instructors.get(0));

        locations.get(1).getLessons().add(s2);
        s2.setLocation(locations.get(1));

        lessons.add(s2);

        SkiLesson s3 = SkiLesson.builder()
                .starttime(LocalDateTime.now())
                .endtime(LocalDateTime.now().plusHours(5))
                .durationMinutes(300)
                .name("Niseko, Japan")
                .price(BigDecimal.valueOf(1250.00))
                .level(Level.ADVANCED)
                .build();

        instructors.get(1).getLessons().add(s3);
        s3.setInstructor(instructors.get(1));

        locations.get(2).getLessons().add(s3);
        s3.setLocation(locations.get(2));

        lessons.add(s3);

        // this lesson will not have an instructor
        SkiLesson s4 = SkiLesson.builder()
                .starttime(LocalDateTime.now())
                .endtime(LocalDateTime.now().plusHours(5))
                .durationMinutes(300)
                .name("Zermatt, Switzerland")
                .price(BigDecimal.valueOf(1120.50))
                .level(Level.ADVANCED)
                .build();

        locations.get(3).getLessons().add(s4);
        s4.setLocation(locations.get(3));

        lessons.add(s4);

        return lessons;
    }
}
