package ru.makarovva.tests;

import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.makarovva.dao.Bookingdates;
import ru.makarovva.dao.CreateBookingRequest;
import ru.makarovva.dao.CreateTokenRequest;
import ru.makarovva.dao.EmptyRequestResponse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;



public class PartialUpdateBookingTests {
    static String token;
    String id;
    static Properties properties = new Properties();
    static String baseUrl;


    @BeforeAll
    static void beforeAll() throws IOException {
        properties.load(new FileInputStream("src\\test\\resources\\application.properties"));
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        baseUrl = properties.getProperty("base.url");
        System.out.println(username);
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
        CreateBookingRequest requestBody = CreateBookingRequest.builder()
                .firstname("Jim")
                .lastname("Brown")
                .totalprice(111)
                .depositpaid(true)
                .bookingdates(Bookingdates.builder().checkin("2012-01-01").checkout("2019-01-01").build())
                .additionalneeds("Breakfast")
                .build();


        //создает бронирование
        id = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(requestBody)
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
    void patchBookingChangeFirstnameAndLastnamePositiveTest() {
        EmptyRequestResponse requestResponse = new EmptyRequestResponse();
        requestResponse.setAdditionalProperty("firstname","John");
        requestResponse.setAdditionalProperty("lastname","Black");
        //создает бронирование
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(requestResponse)
                .expect()
                .statusCode(200)
                .when()
                .patch(baseUrl + "booking/"+ id)
                .prettyPeek()
                .then()
                .extract().response();

        Assertions.assertEquals("John", response.jsonPath().getString("firstname"));
        Assertions.assertEquals("Black", response.jsonPath().getString("lastname"));


    }

    @Test
    void patchBookingChangeOnlyNamePositiveTest() {
        //создает бронирование
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(CreateBookingRequest.builder().firstname("John").build())
                .expect()
                .statusCode(200)
                .when()
                .patch(baseUrl + "booking/"+ id)
                .prettyPeek()
                .then()
                .extract().response();

        Assertions.assertEquals("John", response.jsonPath().getString("firstname"));


    }

    @Test
    void patchBookingChangeOnlyNamePositiveTest2() {
        //создает бронирование
        EmptyRequestResponse emptyRequestResponse =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(CreateBookingRequest.builder().firstname("John").build())
                .expect()
                .statusCode(200)
                .when()
                .patch(baseUrl + "booking/"+ id)
                .prettyPeek()
                .then()
                .extract().response().as(EmptyRequestResponse.class);

        System.out.println(emptyRequestResponse.getAdditionalProperties());

        Assertions.assertEquals("John", emptyRequestResponse.getAdditionalProperties().get("firstname"));


    }
    @Test
    void patchBookingNoChangesPositiveTest() {
        //создает бронирование
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(new EmptyRequestResponse())
                .expect()
                .statusCode(200)
                .when()
                .patch(baseUrl + "booking/"+ id)
                .prettyPeek()
                .then()
                .extract().response();
    }




    @Test
    void patchBookingNoAuthorisationNegativeTest() {
        //создает бронирование
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(CreateBookingRequest.builder().firstname("John").build())
                .expect()
                .statusCode(403)
                .when()
                .patch(baseUrl + "booking/"+ id)
                .prettyPeek()
                .then()
                .extract().response();



    }
    @Test
    void patchBookingNonExistentIdNegativeTest() {
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(CreateBookingRequest.builder().firstname("John").build())
                .expect()
                .statusCode(405)
                .when()
                .patch(baseUrl + "booking/" + "8768675")
                .prettyPeek()
                .then()
                .extract().response();



    }

    @Test
    void patchBookingStringIdNegativeTest() {
        //создает бронирование
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(CreateBookingRequest.builder().firstname("John").build())
                .expect()
                .statusCode(405)
                .when()
                .patch(baseUrl + "booking/" + "gfgfgh")
                .prettyPeek()
                .then()
                .extract().response();



    }
    @Test
    void patchBookingIntFirstnameNegativeTest() {
        EmptyRequestResponse request = new EmptyRequestResponse();
        request.setAdditionalProperty("firstname", 123);
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(request)
                .expect()
                .statusCode(200)
                .when()
                .patch(baseUrl + "booking/"+ id)
                .prettyPeek()
                .then()
                .extract().response();

        Assertions.assertEquals("123", response.jsonPath().getString("firstname"));


    }




}
