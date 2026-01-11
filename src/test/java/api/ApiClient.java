package api;

import config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ApiClient {

    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private final RequestSpecification requestSpec;
    private final ResponseSpecification responseSpec;

    public ApiClient() {
        String baseUri = Config.getProperty("api.base.url", "https://api.example.com");
        int timeout = Config.getIntProperty("api.timeout", 30000);

        RestAssuredConfig config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", timeout)
                        .setParam("http.socket.timeout", timeout));

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .setConfig(config)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();

        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
        logger.info("API Client initialized with base URI: {}", baseUri);
    }

    public Response get(String endpoint) {
        logger.info("GET {}", endpoint);
        return RestAssured.given().get(endpoint);
    }

    public Response get(String endpoint, Map<String, String> queryParams) {
        logger.info("GET {} with params: {}", endpoint, queryParams);
        return RestAssured.given().queryParams(queryParams).get(endpoint);
    }

    public Response post(String endpoint, Object body) {
        logger.info("POST {}", endpoint);
        return RestAssured.given().body(body).post(endpoint);
    }

    public Response put(String endpoint, Object body) {
        logger.info("PUT {}", endpoint);
        return RestAssured.given().body(body).put(endpoint);
    }

    public Response patch(String endpoint, Object body) {
        logger.info("PATCH {}", endpoint);
        return RestAssured.given().body(body).patch(endpoint);
    }

    public Response delete(String endpoint) {
        logger.info("DELETE {}", endpoint);
        return RestAssured.given().delete(endpoint);
    }

    public Response getWithAuth(String endpoint, String token) {
        logger.info("GET {} with auth", endpoint);
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get(endpoint);
    }

    public Response postWithAuth(String endpoint, Object body, String token) {
        logger.info("POST {} with auth", endpoint);
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .post(endpoint);
    }

    public RequestSpecification given() {
        return RestAssured.given().spec(requestSpec);
    }
}
