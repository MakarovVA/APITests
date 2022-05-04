package ru.makarovva.tests;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.makarovva.dao.Bookingdates;
import ru.makarovva.dao.CreateBookingRequest;
import ru.makarovva.dao.CreateTokenRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;




public class DeleteBookingTests {

    static Properties properties = new Properties();
    static String baseUrl;

    static String token;
    String id;

    @BeforeAll
    static void beforeAll() throws IOException {
        properties.load(new FileInputStream("src\\test\\resources\\application.properties"));
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        baseUrl = properties.getProperty("base.url");
        token = given()//предусловия, подготовка
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(CreateTokenRequest.builder().username(username).password(password).build())
                .expect()
                .statusCode(200)
                .body("token", is(CoreMatchers.not(nullValue())))
                .when()
                .post(baseUrl + "auth")//шаг(и)
                .prettyPeek()
                .body()
                .jsonPath()
                .get("token")
                .toString();
    }

    @BeforeEach
    void setUp() {
        //создает бронирование
        id = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(CreateBookingRequest.builder()
                        .firstname("Jim")
                        .lastname("Brown")
                        .totalprice(111)
                        .depositpaid(true)
                        .bookingdates(Bookingdates.builder().checkin("2018-01-01").checkout("2019-01-01").build())
                        .additionalneeds("Breakfast")
                        .build())
                .expect()
                .statusCode(200)
                .when()
                .post(baseUrl + "booking")
                .prettyPeek()
                .body()
                .jsonPath()
                .get("bookingid")
                .toString();
    }

    @Test
    void deleteBookingPositiveTest() {
        given()
                .log()
                .all()
                .header("Cookie", "token=" + token)
                .when()
                .delete(baseUrl + "booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(201);
    }

    @Test
    void deleteBookingNonExistentIdNegativeTest() {
        given()
                .log()
                .all()
                .header("Cookie", "token=" + token)
                .when()
                .delete(baseUrl + "booking/34573496573")
                .prettyPeek()
                .then()
                .statusCode(405);
    }

    @Test
    void deleteBookingStringIdNegativeTest() {
        given()
                .log()
                .all()
                .header("Cookie", "token=" + token)
                .when()
                .delete(baseUrl + "booking/fgjf")
                .prettyPeek()
                .then()
                .statusCode(405);
    }

    @Test
    void deleteBookingNoIdNegativeTest() {
        given()
                .log()
                .all()
                .header("Cookie", "token=" + token)
                .when()
                .delete(baseUrl + "booking")
                .prettyPeek()
                .then()
                .statusCode(404);
    }

    @Test

    void deleteBookingNoAuthorisationNegativeTest() {
        given()
                .log()
                .all()
                .when()
                .delete(baseUrl + "booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(403);

    }
}


