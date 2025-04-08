package app.populators;

import app.entities.Location;
import app.entities.SkiLesson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LocationPopulator
{
    public static List<Location> populate()
    {
        List<Location> locations = new ArrayList<>();
        List<SkiLesson> lessons = SkiLessonPopulator.populate();

        Location l1 = Location.builder()
                .latitude(50.1150d)
                .longitude(122.9500d)
                .lessons(new HashSet<>())
                .build();
        l1.getLessons().add(lessons.get(0));
        locations.add(l1);

        Location l2 = Location.builder()
                .latitude(45.9237d)
                .longitude(6.8694d)
                .lessons(new HashSet<>())
                .build();
        l2.getLessons().add(lessons.get(1));
        locations.add(l2);

        Location l3 = Location.builder()
                .latitude(42.8048d)
                .longitude(140.6874d)
                .lessons(new HashSet<>())
                .build();
        l3.getLessons().add(lessons.get(2));
        locations.add(l3);

        Location l4 = Location.builder()
                .latitude(46.0207d)
                .longitude(7.7491d)
                .lessons(new HashSet<>())
                .build();
        l4.getLessons().add(lessons.get(3));
        locations.add(l4);

        return locations;
    }
}
