package app.populators;

import app.entities.Instructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InstructorPopulator
{
    public static List<Instructor> populate()
    {
        List<Instructor> instructors = new ArrayList<>();

        Instructor i1 = Instructor.builder()
                .firstname("Jens")
                .lastname("Jensen")
                .email("jensen@gmail.com")
                .phone(12345678)
                .yearsOfExperience(10)
                .lessons(new HashSet<>()) // starts with an empty set
                .build();
        instructors.add(i1);

        Instructor i2 = Instructor.builder()
                .firstname("Laura")
                .lastname("Lauritz")
                .email("lala@yahoo.com")
                .phone(87654321)
                .yearsOfExperience(2)
                .lessons(new HashSet<>()) // starts with an empty set
                .build();
        instructors.add(i2);

        Instructor i3 = Instructor.builder()
                .firstname("Peter")
                .lastname("Pedersen")
                .email("pete-123@cphbusiness.dk")
                .phone(32145687)
                .yearsOfExperience(2)
                .lessons(new HashSet<>()) // starts with an empty set
                .build();
        instructors.add(i3);

        return instructors;
    }
}
