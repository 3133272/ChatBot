// Simple String Coloring Class by Dylan Donnell (3145667)
// https://gitlab.griffith.ie/dylan.donnell/color.java

public class Color {
    // Define color Type by enumeration
    enum Type {
        // RESET
        RESET ("\033[0m"),

        // STANDARD
        BLACK ("\033[30m"),
        RED ("\033[31m"),
        GREEN ("\033[32m"),
        YELLOW ("\033[33m"),
        BLUE ("\033[34m"),
        PURPLE ("\033[35m"),
        CYAN ("\033[36m"),
        WHITE ("\033[37m"),

        // BOLD
        BD ("\033[1m"),
        BLACK_BD ("\033[1;30m"),
        RED_BD ("\033[1;31m"),
        GREEN_BD ("\033[1;32m"),
        YELLOW_BD ("\033[1;33m"),
        BLUE_BD ("\033[1;34m"),
        PURPLE_BD ("\033[1;35m"),
        CYAN_BD ("\033[1;36m"),
        WHITE_BD ("\033[1;37m"),

        // UNDERLINED
        UL ("\033[4m"),
        BLACK_UL ("\033[4;30m"),
        RED_UL ("\033[4;31m"),
        GREEN_UL ("\033[4;32m"),
        YELLOW_UL ("\033[4;33m"),
        BLUE_UL ("\033[4;34m"),
        PURPLE_UL ("\033[4;35m"),
        CYAN_UL ("\033[4;36m"),
        WHITE_UL ("\033[4;37m"),

        // BACKGROUND
        BLACK_BG ("\033[40m"),
        RED_BG ("\033[41m"),
        GREEN_BG ("\033[42m"),
        YELLOW_BG ("\033[43m"),
        BLUE_BG ("\033[44m"),
        PURPLE_BG ("\033[45m"),
        CYAN_BG ("\033[46m"),
        WHITE_BG ("\033[47m"),

        // HIGH INTENSITY
        BLACK_HI ("\033[0;90m"),
        RED_HI ("\033[0;91m"),
        GREEN_HI ("\033[0;92m"),
        YELLOW_HI ("\033[0;93m"),
        BLUE_HI ("\033[0;94m"),
        PURPLE_HI ("\033[0;95m"),
        CYAN_HI ("\033[0;96m"),
        WHITE_HI ("\033[0;97m"),

        // BOLD HIGH INTENSITY
        BLACK_BD_HI ("\033[1;90m"),
        RED_BD_HI ("\033[1;91m"),
        GREEN_BD_HI ("\033[1;92m"),
        YELLOW_BD_HI ("\033[1;93m"),
        BLUE_BD_HI ("\033[1;94m"),
        PURPLE_BD_HI ("\033[1;95m"),
        CYAN_BD_HI ("\033[1;96m"),
        WHITE_BD_HI ("\033[1;97m"),

        // BACKGROUND HIGH INTENSITY
        BLACK_BG_HI ("\033[0;100m"),
        RED_BG_HI ("\033[0;101m"),
        GREEN_BG_HI ("\033[0;102m"),
        YELLOW_BG_HI ("\033[0;103m"),
        BLUE_BG_HI ("\033[0;104m"),
        PURPLE_BG_HI ("\033[0;105m"),
        CYAN_BG_HI ("\033[0;106m"),
        WHITE_BG_HI ("\033[0;107m"),

        REVERSED ("\033[7m");

        private final String color; // Stores value of color Type

        // Constructor of Type
        Type (String color) {
            this.color = color;
        }
    }

    // color an input String with Color type
    public static String color(String input, Type type) {
        return type.color + input + Type.RESET.color;      // Return colored String
    }

    // underline an input String
    public static String underline(String input) {
        return Type.UL.color + input + Type.RESET.color;
    }

    // bold an input String
    public static String bold(String input) {
        return Type.BD.color + input + Type.RESET.color;
    }

    // Below is shorthand for Color.color(text, Type.RED_BG_HI)
    public static String error(String input) {
        return color(bold(color(input, Type.BLACK)), Type.RED_BG_HI);
    }

    // Color text in rainbow
    public static String rainbow(String input) {
        // Append all characters to result String
        String result = "";

        // Array of colors to choose from
        Type colors[] = {Type.RED_HI, Type.GREEN_HI, Type.YELLOW_HI, Type.PURPLE_HI, Type.BLUE_HI, Type.CYAN_HI, Type.WHITE_HI};
        int color_length = colors.length;

        // Change color of every character
        char chars[] = input.toCharArray();
        for (int i = 0, j = 0; i != chars.length; ++i, ++j) {
            if (j >= color_length) j = 0; // Loop colors from beginning
            result += colors[j].color + chars[i] + Type.RESET.color; // Append to result and close off with RESET
        }

        // Return rainbowified result
        return result;
    }

    // color a string with Color type and print it
    public static void print(String input, Type type) {
        System.out.print(color(input, type));
    }

    // color a string with Color type and print it
    public static void println(String input, Type type) {
        System.out.println(color(input, type));
    }

    // prefix input string with "Error: ", color it with error colors then print it
    public static void printError(String input) {
        System.out.println(error("Error: " + input));
    }

    // print as ChatBot
    public static void printChatBot(String input) {
        println("Chatbot: " + input, Type.GREEN);
    }
}

