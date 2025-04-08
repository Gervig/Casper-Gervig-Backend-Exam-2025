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
        List<Location> locations = LocationPopulator.populate();

        SkiLesson s1 = SkiLesson.builder()
                .starttime(LocalDateTime.now())
                .endtime(LocalDateTime.now().plusHours(4))
                .durationMinutes(240)
                .name("Whistler Blackcomb, Canada")
                .price(BigDecimal.valueOf(500.00))
                .level(Level.BEGINNER)
                .build();
        lessons.add(s1);

        SkiLesson s2 = SkiLesson.builder()
                .starttime(LocalDateTime.now())
                .endtime(LocalDateTime.now().plusHours(2))
                .durationMinutes(120)
                .name("Chamonix, France")
                .price(BigDecimal.valueOf(750.00))
                .level(Level.INTERMEDIATE)
                .build();
        lessons.add(s2);

        SkiLesson s3 = SkiLesson.builder()
                .starttime(LocalDateTime.now())
                .endtime(LocalDateTime.now().plusHours(5))
                .durationMinutes(300)
                .name("Niseko, Japan")
                .price(BigDecimal.valueOf(1250.00))
                .level(Level.ADVANCED)
                .build();
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
        lessons.add(s4);

        return lessons;
    }
}
