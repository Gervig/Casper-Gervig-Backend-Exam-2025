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

// API url: https://apiprovider.cphbusinessapps.dk/skilesson

public class SkiLessonService
{
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static List<SkiLessonDTO> getLessons(Level level)
    {
        List<SkiLessonDTO> lessonDTOS = new ArrayList<>();
        try
        {
            // http request to get ski lesson for a certain level
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(buildUri(level)))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // if the response gives a success, the status code will be 200
            if (response.statusCode() == 200)
            {
                JsonNode rootNode = objectMapper.readTree(response.body());

                // goes through the results which is inside the array called "lessons"
                for (JsonNode lessonNode : rootNode.path("lessons"))
                {
                    // reads the different values for the keys: title, level and durationMinutes
                    String title = lessonNode.path("title").asText();
                    String lessonLevel = lessonNode.path("level").asText();
                    int duration = lessonNode.path("durationMinutes").asInt();

                    // creates a DTO with the values from the API
                    SkiLessonDTO dto = new SkiLessonDTO();
                    dto.setName(title);
                    dto.setLevel(Level.valueOf(lessonLevel.toUpperCase())); // our enums are uppercase
                    dto.setDurationMinutes(duration);

                    // adds the DTO to the list
                    lessonDTOS.add(dto);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return lessonDTOS;
    }


    // helper method to build uri depending on the level
    public static String buildUri(Level level)
    {
        String levelString = level.toString().toLowerCase(); // API is casesensitive

        String uri = "https://apiprovider.cphbusinessapps.dk/skilesson/%%level";
        uri = uri.replace("%%level", levelString);
        return uri;
    }
}
