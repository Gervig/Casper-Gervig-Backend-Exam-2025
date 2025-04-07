package app.services;

import app.dtos.SkiLessonDTO;
import app.enums.Level;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SkiLessonService
{
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static List<SkiLessonDTO> getLessons(Level level)
    {
        List<SkiLessonDTO> lessonDTOS = new ArrayList<>();
        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(buildUri(level)))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200)
            {
                JsonNode rootNode = objectMapper.readTree(response.body());

                for (JsonNode lessonNode : rootNode.path("lessons"))
                {
                    String title = lessonNode.path("title").asText();
                    String description = lessonNode.path("description").asText();
                    String lessonLevel = lessonNode.path("level").asText();
                    int duration = lessonNode.path("durationMinutes").asInt();

                    // Optional fields like createdAt/updatedAt if your DTO uses them
                    // LocalDateTime createdAt = LocalDateTime.parse(lessonNode.path("createdAt").asText());

                    SkiLessonDTO dto = new SkiLessonDTO();
                    dto.setName(title);
                    dto.setLevel(Level.valueOf(lessonLevel.toUpperCase())); // be sure your enum matches
                    dto.setDurationMinutes(duration);

                    lessonDTOS.add(dto);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return lessonDTOS;
    }


    public static String buildUri(Level level)
    {
        String levelString = level.toString().toLowerCase(); // API is casesensitive

        String uri = "https://apiprovider.cphbusinessapps.dk/skilesson/%%level";
        uri = uri.replace("%%level", levelString);
        return uri;
    }
}
