package app.dtos;

import app.entities.SkiLesson;
import app.enums.Level;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties
public class SkiLessonDTO
{
    private Long id;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private double longitude;
    private double latitude;
    private String name;
    private BigDecimal price;
    private Level level;

    private InstructorDTO instructor;

    public SkiLessonDTO(SkiLesson skiLesson, boolean includeDetails)
    {
        this.id = skiLesson.getId();
        this.starttime = skiLesson.getStarttime();
        this.endtime = skiLesson.getEndtime();
        this.longitude = skiLesson.getLongitude();
        this.latitude = skiLesson.getLatitude();
        this.name = skiLesson.getName();
        this.price = skiLesson.getPrice();
        this.level = skiLesson.getLevel();

        if (includeDetails && skiLesson.getInstructor() != null)
        {
            this.instructor = new InstructorDTO(skiLesson.getInstructor(), false);
            this.instructor.getLessons().add(this);
        }

    }

    public SkiLessonDTO toEntity()
    {
        SkiLesson lesson = SkiLesson.builder()
                .id(this.id)
                .starttime(this.starttime)
                .endtime(this.endtime)
                .longitude(this.longitude)
                .latitude(this.latitude)
                .name(this.name)
                .price(this.price)
                .level(this.level)
                .build();
    }
}
