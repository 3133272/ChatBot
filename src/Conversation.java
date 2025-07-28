import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Random;

public class Conversation {
    // print out a random greeting from a list of greetings
    static String greeting() {
        final String []GREETINGS = {
                "Hello, I am your personal Travelling ChatBot, ask me something",
                "Hello! What could I help you with today?",
                "Greetings, how could I help you today?",
                "Welcome! Please ask me any questions you may have.",
                "Hello there, would like help with something?",
                "Pleased to meet you! Feel free to ask me any questions you may have."
        };

        Random r = new Random();
        return GREETINGS[r.nextInt(GREETINGS.length)];
    }

    // ask user for missing info
    static String missingInfo(String info) {
        final String []INFO_FORMAT = {
                "Apologies, could you provide info on the %s of you travel?",
                "Could you tell me what %s you are looking for?",
                "Any ideas about %s?",
                "Could you provide the missing information of %s so I can help you further?",
                "Please tell me what %s you were looking for so I can search this information up for you.",
                "No problem, can you tell me the following information: %s"
        };

        // format with random INFO_FORMAT string
        Random r = new Random();
        return String.format(INFO_FORMAT[r.nextInt(INFO_FORMAT.length)], info);
    }

    // try to parse city name from input string
    static String tryParseCity(String input) {
        final String []split = input.split(" ");

        // assume city is the only word if split is length 1
        if (split.length == 1)
            return input;

        // assume city follows "to" and is not "go"
        for (int i = 0; i != split.length - 1; ++i)
            if (split[i].equalsIgnoreCase("to"))
                if (!split[i + 1].equalsIgnoreCase("go"))
                    return split[i + 1];

        return "";
    }

    // get start and end dates if applicable
    static int []tryParseDates(String input) {
        final String []split = input.split(" ");

        final String []DAYS = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        final LocalDate today = LocalDate.now();
        final DayOfWeek todayDay = today.getDayOfWeek();
        final int todayDayOfMonth = today.getDayOfMonth();
        int todayDayIndex = -1;
        for (int i = 0; i != DAYS.length; ++i)
            if (DAYS[i].equalsIgnoreCase(todayDay.toString())) {
                todayDayIndex = i;
                break;
            }

        int []result = new int[2];
        int ri = 0;

        for (int i = 0; i != split.length; ++i) {
            // start and end days found
            if (ri == 2)
                break;

            // check if token is in DAYS
            for (int j = 0; j != DAYS.length; ++j) {
                if (split[i].equalsIgnoreCase(DAYS[j])) {
                    if (todayDayIndex == j)
                        result[ri++] = 0;
                    else if (todayDayIndex < j)
                        result[ri++] = j - todayDayIndex;
                    else
                        result[ri++] = todayDayIndex - j;
                    break;
                }
            }

            // try to parse from number
            try {
                int dateNth = Integer.parseInt(split[i].replace("th", "").replace("st", ""));
                if (todayDayOfMonth == dateNth)
                    result[ri++] = 0;
                else if (todayDayOfMonth < dateNth)
                    result[ri++] = dateNth - todayDayOfMonth;
                else
                    result[ri++] = dateNth + 30 - todayDayOfMonth;
            } catch (NumberFormatException ignored) {} // ignore number format exception
        }

        return result;
    }
}
