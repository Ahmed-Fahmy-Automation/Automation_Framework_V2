package tests.api;

import api.ApiClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("API")
@Feature("Booking API")
public class ApiBookingTest {

    private ApiClient apiClient;

    @BeforeClass
    public void setup() {
        apiClient = new ApiClient();
    }

    @Test(description = "Verify GET all bookings returns 200")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Bookings")
    public void testGetAllBookings() {
        Response response = apiClient.get("/bookings");

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertNotNull(response.jsonPath().getList("$"), "Response should contain bookings array");
    }

    @Test(description = "Verify GET booking by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Booking")
    public void testGetBookingById() {
        Response response = apiClient.get("/bookings/1");

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertEquals(response.jsonPath().getInt("id"), 1, "Booking ID should match");
    }

    @Test(description = "Verify POST create new booking")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create Booking")
    public void testCreateBooking() {
        Map<String, Object> booking = new HashMap<>();
        booking.put("firstName", "Test");
        booking.put("lastName", "User");
        booking.put("roomType", "Standard");
        booking.put("checkInDate", "2026-03-01");
        booking.put("checkOutDate", "2026-03-05");

        Response response = apiClient.post("/bookings", booking);

        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201");
        Assert.assertNotNull(response.jsonPath().getInt("id"), "Response should contain booking ID");
    }

    @Test(description = "Verify DELETE booking")
    @Severity(SeverityLevel.NORMAL)
    @Story("Delete Booking")
    public void testDeleteBooking() {
        Response response = apiClient.delete("/bookings/999");

        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 404,
            "Status should be 200 or 404");
    }
}
