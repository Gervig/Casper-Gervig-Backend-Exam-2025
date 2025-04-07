package app.entities;

import app.enums.Level;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Entity
public class SkiLesson
{
    // all attributes have setters, except id. I could have added @Setter to the class otherwise
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private LocalDateTime starttime;
    @Setter
    private LocalDateTime endtime;
    @Setter
    private double longitude;
    @Setter
    private double latitude;
    @Setter
    private String name;
    @Setter
    private BigDecimal price;
    @Setter
    private Level level;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @Setter
    private Instructor instructor;

}
