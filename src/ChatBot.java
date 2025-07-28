
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatBot {
	public static void main(String[] args) {
		// Open scanner to let user input cities
		Scanner myscanner = new Scanner(System.in);
		// initial conversation
		Color.println("-----------------------------------------------", Color.Type.GREEN);
		Color.printChatBot(Conversation.greeting());
		String inputOne = myscanner.nextLine();
		System.out.println("ChatBot:Sure, Can you tell me the cities you want to go.");
		try {
			System.out.println("Or input 'no' to quit");
			// Open scanner to let user input cities
			Scanner scanner = new Scanner(System.in);

			// arrayList to store cities' name
			ArrayList<String> city = new ArrayList<>();

			// index for five city for while loop
			int index = 1;

			// using do while loop to let user quit if input: No
			while (index != 5) {
				// get user input
				Color.print("> ", Color.Type.CYAN);
				String input = scanner.nextLine();

				// no input means we should get another line of input
				if (input.isEmpty())
					continue;

				// if inputted data == 'No' then print out a message and quit the loop
				if (input.equalsIgnoreCase("No")) {
					Color.println("You have quited", Color.Type.CYAN);
					break;
				}

				// parse the city name from the input
				String cityName = Conversation.tryParseCity(input);
				if (cityName.isEmpty()) {
					Color.printChatBot(Conversation.missingInfo("City"));
					continue;
				}

				// Get location data
				// create JsonObject to get the Location
				JSONObject cityLocationData = getLocationData(cityName);

				// invalid location means we should not continue further
				if (cityLocationData == null) {
					Color.printError("Location \"" + input + "\" was not found, please ensure the spelling is correct and re-enter your selection.");
					continue;
				}

				Color.printChatBot("Found a city with name \"" + cityName + "\", where else would you like to go?");

				// after checking the city name then
				// store the valid city name to the arrayList
				city.add(cityName);

				// city index plus one
			    index++;
			}

			Color.printChatBot("Here is the weather condition and forecast:\n");

			// for loop to print out weather conditions based on how many cities in that arrayList
			for (String c : city)
				// each city call the printCityResult() method to display weather condition and next three days' forecast
				printCityResult(c);

			// catch potential Exception
		} catch (Exception e) {
			Color.printError(e.toString());
		}
	}

	// method to get weather information based on a city name
	// call this method five times for five different cities
	public static void printCityResult(String city) {

		// using try() and catch() to handle necessary exceptions.
		try {

				// Get location data
				// create JsonObject to get the Location
				JSONObject cityLocationData = getLocationData(city);

				// invalid location means we should not continue further
				if (cityLocationData == null) {
					Color.printError("Location \"" + city + "\" was not found, please ensure the spelling is correct and re-enter your selection.");
				}

				// get the latitude of the city
				double latitude = (double) cityLocationData.get("latitude");
				// get the longitude of the city
				double longitude = (double) cityLocationData.get("longitude");

				// print out the city's name and location:
				Color.println("City: " + city, Color.Type.CYAN);
                //Color.println("Latitude: " + latitude, Color.Type.CYAN);
                //Color.println("Longitude: " + longitude, Color.Type.CYAN);
				// implementation of displayWeatherData() method
				displayWeatherData(latitude, longitude);

				// Derrick Longkai Zhang 3133272
				// add weather forecast
				// convert double latitude and longitude to String
				String latitude1 = Double.toString(latitude);
				String longitude1 = Double.toString(longitude);
				// test weatherForecast class
				// create an instance of WeatherForecast class
				WeatherForecast cityOneForecast = new WeatherForecast(latitude1, longitude1);

				// call the weatherForecast() method in the class to display the weather forecast of the next 4 days starting today
				Color.println("Weather Forecast:", Color.Type.CYAN);

				// print out the forecast for the next 4 days starting today
				System.out.println(cityOneForecast.forecast(0, 4));

		} catch (Exception e) {
			Color.printError(e.toString());
		}
	}

	// Derrick Longkai Zhang 3133272
	// method getLocationData()
	// type of the method is JSONObject with one argument: String city
	public static JSONObject getLocationData(String city) {
		// regex expression to trim the city format
		city = city.replaceAll(" ", "+");
		// getting the json city url and store it as a string
		String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
				city + "&count=1&language=en&format=json";

		try {
			// step1. Fetch the API response based on API Link
			HttpURLConnection apiConnection = fetchApiResponse(urlString);

			// check for response status
			// 200 - means that the connection was a success
			if (apiConnection.getResponseCode() != 200) {
				Color.printError("Could not connect to geography API.");
				return null;
			}

			// step2. Read the response and convert store String type
			String jsonResponse = readApiResponse(apiConnection);

			// step3. Parse the string into a JSON Object
			JSONParser parser = new JSONParser();
			JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

			// step4. Retrieve Location Data
			JSONArray locationData = (JSONArray) resultsJsonObj.get("results");

			// ensure results have been found before returning them
			if (locationData == null) {
				Color.printError("No results were found for the city query \"" + city + "\"");
				return null;
			}

			return (JSONObject) locationData.get(0);
		} catch (Exception e) {
			Color.printError(e.toString());
		}
		return null;
	}

	// Derrick Longkai Zhang 3133272
	// fetchApiResponse() method with String argument: the json city url
	private static HttpURLConnection fetchApiResponse(String urlString) {
		try {
			// attempt to connect to the API
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// set request method to get data from the API url
			connection.setRequestMethod("GET");
			// return the connection condition
			return connection;
		} catch (IOException e) {
			Color.printError(e.toString());
		}

		// connection fail
		return null;
	}

	// Derrick Longkai Zhang 3133272
	// method readApiResponse with a HttpURLConnection argument apiConnection
	private static String readApiResponse(HttpURLConnection apiConn) {
		try {
			// Create a StringBuilder to store the resulting JSON data
			StringBuilder result = new StringBuilder();

			// Use a Scanner to read the InputStream from the HttpURLConnection
			Scanner scanner = new Scanner(apiConn.getInputStream());

			// Loop through each line in the response and append it to the StringBuilder
			while (scanner.hasNext()) {
				// Append the current line to the StringBuilder
				result.append(scanner.nextLine());
			}
			// close scanner
			scanner.close();

			// Return the JSON data as a String
			return result.toString();

		} catch (IOException e) {
			Color.printError(e.toString());
		}
		// Return null if there was an issue reading the response
		return null;
	}

	// Lincoln Teo Ming Kern 3122215
	// method displayWeatherData() with two arguments: double latitude, double longitude
	private static void displayWeatherData(double latitude, double longitude) {
		try {
			//Get the API response from the OpenWeatherMap API
			// with two arguments: latitude and longitude to input the corresponding coordinates into the API link
			String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=ecfb062d63ee7dbdea0650c7d7f89eb4";
			HttpURLConnection apiConnection = fetchApiResponse(url);

			// check for response status code
			// 200 - means that the request was successful
			if (apiConnection.getResponseCode() != 200) {
				Color.printError("Could not connect to API Weather");
				return;
			}

			// Read the response and convert to String type
			String jsonResponse = readApiResponse(apiConnection);

			// Parse the string into a JSON Object
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
			JSONArray list = (JSONArray) jsonObject.get("list");

			// Store the data into variables
			if (list != null && !list.isEmpty()) {
				//retrieves the first element from the JSON array list
				JSONObject firstEntry = (JSONObject) list.get(0);
				// extracts the main JSON object from the firstEntry JSON object
				JSONObject main = (JSONObject) firstEntry.get("main");

				// get current temp from the Json Object named main
				double temperature = (double) main.get("temp");
				Color.println("Current Temperature (C): " + temperature, Color.Type.CYAN);

				// get minimum temp from the Json Object named main
				double tempMin = (double) main.get("temp_min");
				Color.println("Minimum Temperature (C): " + tempMin, Color.Type.CYAN);

				// get max temp from the Json Object named main
				double tempMax = (double) main.get("temp_max");
				Color.println("Maximum Temperature (C): " + tempMax, Color.Type.CYAN);

				// get humidity from the Json Object named main
				long humidity = (long) main.get("humidity");
				Color.println("Humidity: " + humidity, Color.Type.CYAN);

				//code extracts the wind JSON object from the firstEntry:
				JSONObject wind = (JSONObject) firstEntry.get("wind");
				// get wind speed from the Json Object named wind
				double windSpeed = (double) wind.get("speed");
				Color.println("Wind Speed (Km/H): " + windSpeed, Color.Type.CYAN);
			}
		} catch (Exception e) {
			Color.printError(e.toString());
		}
	}
}