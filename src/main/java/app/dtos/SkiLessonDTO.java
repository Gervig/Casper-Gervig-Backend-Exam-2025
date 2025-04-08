package app.dtos;

import app.entities.Instructor;
import app.entities.SkiLesson;
import app.enums.Level;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String name;
    private BigDecimal price;
    private Level level;
    private int durationMinutes;

    private LocationDTO location;

    private InstructorDTO instructor;

    public SkiLessonDTO(SkiLesson skiLesson, boolean includeDetails)
    {
        this.id = skiLesson.getId();
        this.starttime = skiLesson.getStarttime();
        this.endtime = skiLesson.getEndtime();
        this.name = skiLesson.getName();
        this.price = skiLesson.getPrice();
        this.level = skiLesson.getLevel();

        if (includeDetails && skiLesson.getInstructor() != null)
        {
            this.instructor = new InstructorDTO(skiLesson.getInstructor(), false);
//            this.instructor.getLessons().add(this); //TODO remove this if not needed
        }

        if(includeDetails && skiLesson.getLocation() != null)
        {
            this.location = new LocationDTO(skiLesson.getLocation(), false);
//            this.location.getLessons().add(this); //TODO remove this if not needed
        }

    }

    public SkiLesson toEntity()
    {
        SkiLesson lesson = SkiLesson.builder()
                .id(this.id)
                .starttime(this.starttime)
                .endtime(this.endtime)
                .name(this.name)
                .price(this.price)
                .level(this.level)
                .build();
        // checks if the lesson has an instructor
        if(this.instructor != null)
        {
            // creates an instructor entity
            Instructor instructorEntity = this.instructor.toEntity();
            // sets the instructor for the lesson
            lesson.setInstructor(instructorEntity);
            // adds the lesson to the instructor
            instructorEntity.addLesson(lesson);
        }
        return lesson;
    }
}
