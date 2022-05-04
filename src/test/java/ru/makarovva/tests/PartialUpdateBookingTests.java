package ru.makarovva.tests;

import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;



public class PartialUpdateBookingTests {
    static String token;
    String id;

    @BeforeAll
    static void beforeAll() {
        token = given()//предусловия, подготовка
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body("{\n"
                        + "    \"username\" : \"admin\",\n"
                        + "    \"password\" : \"password123\"\n"
                        + "}")
                .expect()
                .statusCode(200)
                .body("token", is(CoreMatchers.not(nullValue())))
                .when()
                .post("https://restful-booker.herokuapp.com/auth")//шаг(и)
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
                .body("{\n"
                        + "    \"firstname\" : \"Jim\",\n"
                        + "    \"lastname\" : \"Brown\",\n"
                        + "    \"totalprice\" : 111,\n"
                        + "    \"depositpaid\" : true,\n"
                        + "    \"bookingdates\" : {\n"
                        + "        \"checkin\" : \"2018-01-01\",\n"
                        + "        \"checkout\" : \"2019-01-01\"\n"
                        + "    },\n"
                        + "    \"additionalneeds\" : \"Breakfast\"\n"
                        + "}")
                .expect()
                .statusCode(200)
                .when()
                .post("https://restful-booker.herokuapp.com/booking")
                .prettyPeek()
                .body()
                .jsonPath()
                .get("bookingid")
                .toString();
    }

    @Test
    void patchBookingChangeFirstnameAndLastnamePositiveTest() {
        //создает бронирование
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : \"John\",\n"
                        + "    \"lastname\" : \"Black\"\n"
                        + "}")
                .expect()
                .statusCode(200)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
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
                .body("{\n"
                        + "    \"firstname\" : \"John\"\n"
                        + "}")
                .expect()
                .statusCode(200)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek()
                .then()
                .extract().response();

        Assertions.assertEquals("John", response.jsonPath().getString("firstname"));


    }

    @Test
    void patchBookingNoChangesPositiveTest() {
        //создает бронирование
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body("{\n"
                        + "}")
                .expect()
                .statusCode(200)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
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
                .body("{\n"
                        + "    \"firstname\" : \"John\"\n"
                        + "}")
                .expect()
                .statusCode(403)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek()
                .then()
                .extract().response();



    }
    @Test
    void patchBookingNonExistentIdNegativeTest() {
        //создает бронирование
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : \"John\"\n"
                        + "}")
                .expect()
                .statusCode(405)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/8768675")
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
                .body("{\n"
                        + "    \"firstname\" : \"John\"\n"
                        + "}")
                .expect()
                .statusCode(405)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/gfgfgh")
                .prettyPeek()
                .then()
                .extract().response();



    }
    @Test
    void patchBookingIntFirstnameNegativeTest() {
        //создает бронирование
        Response response =given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body("{\n"
                        + "    \"firstname\" : 123\n"
                        + "}")
                .expect()
                .statusCode(200)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/"+ id)
                .prettyPeek()
                .then()
                .extract().response();

        Assertions.assertEquals("123", response.jsonPath().getString("firstname"));


    }




}
