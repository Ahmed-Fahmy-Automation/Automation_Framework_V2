package data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import config.Config;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestData {

    private static final Gson gson = new Gson();

    public static String getTestUsername() {
        return Config.getProperty("test.username", "testuser");
    }

    public static String getTestPassword() {
        return Config.getProperty("test.password", "testpass123");
    }

    public static JsonObject loadJsonFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON file: " + filePath, e);
        }
    }

    public static JsonArray getBookingsData() {
        JsonObject data = loadJsonFile("src/test/resources/testdata/booking.json");
        return data.getAsJsonArray("bookings");
    }

    public static JsonArray getUsersData() {
        JsonObject data = loadJsonFile("src/test/resources/testdata/booking.json");
        return data.getAsJsonArray("users");
    }

    public static JsonObject getBookingById(int id) {
        JsonArray bookings = getBookingsData();
        for (int i = 0; i < bookings.size(); i++) {
            JsonObject booking = bookings.get(i).getAsJsonObject();
            if (booking.get("id").getAsInt() == id) {
                return booking;
            }
        }
        return null;
    }

    public static JsonObject getUserByUsername(String username) {
        JsonArray users = getUsersData();
        for (int i = 0; i < users.size(); i++) {
            JsonObject user = users.get(i).getAsJsonObject();
            if (user.get("username").getAsString().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static <T> T loadJsonAs(String filePath, Class<T> clazz) {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON file: " + filePath, e);
        }
    }

    public static String readFileAsString(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }
}
