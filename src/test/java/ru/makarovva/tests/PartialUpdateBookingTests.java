package ru.makarovva.tests;

import io.qameta.allure.*;
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

@Severity(SeverityLevel.BLOCKER)
@Story("Partial booking update")
@Feature("Tests for booking partial update")


public class PartialUpdateBookingTests extends BaseTest {

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
    @Description("Partial booking update for firstname and lastname")
    @Step("Patch firstname and lastname")
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
    @Description("Partial booking update for firstname")
    @Step("Patch firstname")
    void patchBookingChangeOnlyFirstnamePositiveTest() {
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
    @Description("Partial booking update without any changes")
    @Step("Patch without any changes")
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
    @Description("Partial booking update for firstname without authorisation")
    @Step("Patch firstname without authorisation")
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
    @Description("Partial update for firstname with non existent booking id")
    @Step("Patch firstname update for firstname with non existent booking id")
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
    @Description("Partial update for firstname with string id")
    @Step("Patch firstname for firstname with string id")
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
    @Description("Partial update for firstname as int")
    @Step("Patch firstname as int")
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
