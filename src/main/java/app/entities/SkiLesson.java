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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private double longitude;
    private double latitude;
    private String name;
    private BigDecimal price;
    private Level level;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @Setter
    private Instructor instructor;

}
