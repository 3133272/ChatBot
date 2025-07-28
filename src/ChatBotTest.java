import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

class ChatBotTest {
    final String[] CITIES = {
            "Cork", "London", "Las Vegas", "Fake City 10000"
    };

    @Test
    void getLocationData() {
        int i = 0;
        // loop through each city
        while (i != CITIES.length) {
            // get the json response for each city
            JSONObject json = ChatBot.getLocationData(CITIES[i++]);

            // fake city should not return value
            if (i == CITIES.length) {
                assertNull(json);
                break;
            }

            // currently used keys, can be added to if more of the json response is used
            assertNotNull(json);
            assertNotNull(json.get("latitude"));
            assertNotNull(json.get("longitude"));
        }
    }

}
