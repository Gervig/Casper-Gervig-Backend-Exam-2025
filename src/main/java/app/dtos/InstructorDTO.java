package app.dtos;

import app.entities.Instructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties
public class InstructorDTO
{
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private int phone;
    private int yearsOfExperience;

    @ToString.Exclude //avoids infinite to.String recursion
    @JsonIgnore // avoids stackoverflow when creating JSON
    private Set<SkiLessonDTO> lessons;

    public InstructorDTO(Instructor instructor, boolean includeDetails)
    {
        this.id = instructor.getId();
        this.firstname = instructor.getFirstname();
        this.lastname = instructor.getLastname();
        this.email = instructor.getEmail();
        this.phone = instructor.getPhone();
        this.yearsOfExperience = instructor.getYearsOfExperience();

        // maps the SkiLessons for an instructor to DTOs if they have any
        if(includeDetails && !instructor.getLessons().isEmpty())
        {
            this.lessons = instructor.getLessons()
                    .stream()
                    .map(skiLesson -> new SkiLessonDTO(skiLesson, false))
                    .collect(Collectors.toSet());
        }
    }

    public Instructor toEntity()
    {
        Instructor instructor = Instructor.builder()
                .id(this.id)
                .firstname(this.firstname)
                .lastname(this.lastname)
                .email(this.email)
                .phone(this.phone)
                .yearsOfExperience(this.yearsOfExperience)
                .lessons(new HashSet<>()) // gives the instructor an empty set
                .build();

        return instructor;
    }
}
