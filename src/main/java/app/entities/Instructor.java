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
@Table(name= "instructor", uniqueConstraints = {
        @UniqueConstraint(name = "unique_firstname_lastname_email", columnNames = {"firstname", "lastname", "email"})
})
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

    @OneToMany(mappedBy = "instructor" , fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<SkiLesson> lessons = new HashSet<>(); // instantiates it so that the set is never null, but just empty

    public void addLesson(SkiLesson lesson)
    {
        // adds the lesson to the instructor
        this.lessons.add(lesson);
        // sets the instructor for the lesson
        lesson.setInstructor(this);
    }
}
