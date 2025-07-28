import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.*;
import org.junit.jupiter.api.Test;

public class WeatherForecastTest {
    WeatherForecast w1;
    WeatherForecast w0;
    WeatherForecast w2;

    WeatherForecastTest () {
        // json object
        JSONObject obj = new JSONObject();
        obj.put("day", "Test Day");
        obj.put("weather", "Test Weather");
        obj.put("summary", "Test Summary");
        obj.put("feels_like", 20.0);

        // create size 1 array
        {
            JSONArray json = new JSONArray();
            json.add(obj);
            w1 = new WeatherForecast(json);
        }

        // create size 0 array
        {
            JSONArray json = new JSONArray();
            w0 = new WeatherForecast(json);
        }

        // create size 2 array
        {
            JSONArray json = new JSONArray();
            json.add(obj);
            json.add(obj);
            w2 = new WeatherForecast(json);
        }
    }

    @Test
    void validDay() {
        // single day
        assertTrue(w1.validDay(0));
        assertFalse(w1.validDay(1));
        assertFalse(w1.validDay(-1));

        // 0 days
        assertFalse(w0.validDay(0));
        assertFalse(w0.validDay(1));
        assertFalse(w0.validDay(-1));

        // 2 days
        assertTrue(w2.validDay(0));
        assertTrue(w2.validDay(1));
        assertFalse(w2.validDay(2));
        assertFalse(w2.validDay(-1));
        assertFalse(w2.validDay(-2));
    }

    @Test
    void clothingSuggestion() {
        // empty weather
        assertEquals("", w0.clothingSuggestion(0));
        assertEquals("", w0.clothingSuggestion(1));
        assertEquals("", w0.clothingSuggestion(-1));

        // single day
        assertEquals("Wear light clothing.", w1.clothingSuggestion(0));
        assertEquals("", w1.clothingSuggestion(1));

        // create a json array with values -6, -1, 4, 9, 14, 19
        JSONArray json = new JSONArray();
        for (double i = -6; i < 20; i += 5) {
            JSONObject obj = new JSONObject();
            obj.put("feels_like", i);
            json.add(obj);
        }
        WeatherForecast wTemp = new WeatherForecast(json);

        assertEquals("Wear a heavy coat and warm clothing.", wTemp.clothingSuggestion(0));
        assertEquals("Wear a heavy coat and warm clothing.", wTemp.clothingSuggestion(1));
        assertEquals("Wear a heavy coat and warm clothing.", wTemp.clothingSuggestion(2));
        assertEquals("Wear a jacket and warm clothing.", wTemp.clothingSuggestion(3));
        assertEquals("Wear a light jacket or sweater.", wTemp.clothingSuggestion(4));
        assertEquals("Wear light clothing.", wTemp.clothingSuggestion(5));
    }

    @Test
    void forecast() {
        // empty list
        assertEquals("", w0.forecast(0));
        assertEquals("", w0.forecast(1));
        assertEquals("", w0.forecast(-1));
        assertEquals("", w0.forecast(-1));
    }

    @Test
    void to_string() {
        // empty list
        assertEquals("", w0.toString());
    }
}
