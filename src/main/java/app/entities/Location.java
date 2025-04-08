package app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Entity
public class Location
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "location")
    @ToString.Exclude
    private Set<SkiLesson> lessons;

    public void addLesson(SkiLesson lesson)
    {
        this.lessons.add(lesson);
        lesson.setLocation(this);
    }
}
