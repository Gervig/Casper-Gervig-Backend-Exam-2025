package app.dtos;

import app.entities.Location;
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
public class LocationDTO
{
    private Long id;
    private double latitude;
    private double longitude;

    @ToString.Exclude
    @JsonIgnore
    private Set<SkiLessonDTO> lessons = new HashSet<>();

    public LocationDTO(Location location, boolean includeDetails)
    {
        this.id = location.getId();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();

        if(includeDetails && !location.getLessons().isEmpty())
        {
            this.lessons = location.getLessons()
                    .stream()
                    .map(skiLesson -> new SkiLessonDTO(skiLesson, false))
                    .collect(Collectors.toSet());
        }
    }

    public Location toEntity()
    {
        Location location = Location.builder()
                .id(this.id)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .lessons(new HashSet<>())
                .build();

        return location;
    }

}
