package app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Set;

@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Entity
public class Instructor
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private int phone;
    private int yearsOfExperience;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.PERSIST) // CascadeType.PERSIST, when an instructor is persisted so is their lessons
    private Set<SkiLesson> lessons = new HashSet<>(); // instantiates it so that the set is never null, but just empty

    public void addLesson(SkiLesson lesson)
    {
        this.lessons.add(lesson);
    }
}
