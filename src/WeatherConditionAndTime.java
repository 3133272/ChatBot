import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherConditionAndTime {
    // Derrick, Longkai Zhang 3133272
    // displayWeatherConditionAndTime() method is to display the current weather condition and local time based on latitude and longitude
    // based on latitude and longitude using and weather API request.
    // @throws IOException           If an input/output error occurs
    // @throws InterruptedException  If the request is interrupted
    public static void displayWeatherConditionAndTime(String latitude,String longitude) throws IOException, InterruptedException{

        // Construct the API request URL using latitude and longitude
        String u = "https://easy-weather1.p.rapidapi.com/daily/5?latitude="+latitude+"&longitude="+longitude;

        // Create HTTP request object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(u))
                .header("x-rapidapi-key", "3ec6205bf9msh83861deaecbd4b8p1ad8b7jsnedd27a4f8084")// API Key (probably should be stored in a more secure way)
                .header("x-rapidapi-host", "easy-weather1.p.rapidapi.com")// API Host
                .method("GET", HttpRequest.BodyPublishers.noBody())// HTTP GET Request
                .build();
        // Send the request and receive the response
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Store the response body as a string
        String body = response.body();

        // Extract the weather condition from the JSON response
        String key1 = "\"conditionCode\":";
        int startIndex1 = body.indexOf(key1);
        if (startIndex1 != -1) {
            startIndex1 += key1.length();
            int endIndex1 = body.indexOf(",", startIndex1);
            if (endIndex1 == -1) { // In case temperature is the last key
                endIndex1 = body.indexOf("}", startIndex1);
            }
            String condition = body.substring(startIndex1, endIndex1).trim();
            System.out.println("Current Weather Condition: " + condition);
        } else {
            System.out.println("Temperature key not found.");
        }

        // Extract the readTime (current time) from the JSON response
        String key2 = "\"readTime\":";
        int startIndex2 = body.indexOf(key2);
        if (startIndex2 != -1) {
            startIndex2 += key2.length();
            int endIndex3 = body.indexOf(",", startIndex2);
            if (endIndex3 == -1) { // In case temperature is the last key
                endIndex3 = body.indexOf("}", startIndex2);
            }
            String time = body.substring(startIndex2, endIndex3).trim();
            System.out.println("Current Time: " + time);
        } else {
            System.out.println("readTime key not found.");
        }
    }
}
