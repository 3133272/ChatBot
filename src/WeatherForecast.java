import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.http.HttpClient.newHttpClient;

public class WeatherForecast {
    private JSONArray data;

    public WeatherForecast(JSONArray data) {
        this.data = data;
    }

    public WeatherForecast(String latitude,String longitude) throws IOException, InterruptedException {
        // Construct API URL with latitude and longitude parameters
        String url = "https://ai-weather-by-meteosource.p.rapidapi.com/daily?lat="+latitude+"&lon="+longitude+"&language=en&units=auto";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", "3ec6205bf9msh83861deaecbd4b8p1ad8b7jsnedd27a4f8084")
                .header("x-rapidapi-host", "ai-weather-by-meteosource.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Store response body for processing
        String body = response.body();

        // get the data JSONArray
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(body);
            JSONObject daily = (JSONObject) jsonObject.get("daily");
            data = (JSONArray) daily.get("data");
        } catch (ParseException e) {
            Color.printError(e.toString());
        }
    }

    public boolean validDay(int n) {
        if (n < 0 || n >= data.size()) {
            Color.error("Day " + n + " is out of range of size " + data.size());
            return false;
        }
        return true;
    }

    // get the clothing suggestion for day n
    public String clothingSuggestion(int n) {
        // day must fall within range
        if (!validDay(n)) {
            return "";
        }

        // get the feels_like parameter
        JSONObject selected = (JSONObject) data.get(n);
        double feelsLike = (double) selected.get("feels_like");

        // Suggest appropriate clothing based on the feels_like temperature
        if (feelsLike < 5.0) {
            return "Wear a heavy coat and warm clothing.";
        } else if (feelsLike < 10.0) {
            return "Wear a jacket and warm clothing.";
        } else if (feelsLike < 15.0) {
            return "Wear a light jacket or sweater.";
        } else {
            return "Wear light clothing.";
        }
    }

    // get the forecast for day n
    public String forecast(int n) {
        // invalid day
        if (!validDay(n)) {
            return "";
        }

        // get the corresponding day
        JSONObject current = (JSONObject) data.get(n);

        // return formatted data
        return Color.color("\nDate: "              + current.get("day"),     Color.Type.WHITE)  + "\n" +
               Color.color("Weather condition: "   + current.get("weather"), Color.Type.PURPLE) + "\n" +
               Color.color("Weather Description: " + current.get("summary"), Color.Type.YELLOW) + "\n" +
               Color.color("Clothing Suggestion: " + clothingSuggestion(n),  Color.Type.GREEN)  + "\n";
    }

    // get the forecast between begin and end days, 0 corresponds to today
    public String forecast(int begin, int end) {
        // invalid begin or end day
        if (!(validDay(begin) || validDay(end))) {
            return "";
        }

        // ensure end is after begin
        if (end < begin) {
            Color.printError("Begin date cannot be before end");
            return "";
        }

        // append each day's forecast to the result
        StringBuilder result = new StringBuilder();
        for (int i = begin; i != end; ++i)
            result.append(forecast(i));

        // return the result
        return result.toString();
    }

    // get the full data
    @Override
    public String toString() {
        return forecast(0, data.size());
    }
}
