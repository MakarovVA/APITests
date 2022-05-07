package ru.makarovva.tests;

import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
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

@Severity(SeverityLevel.BLOCKER)
@Story("delete a booking")
@Feature("Tests for booking deletion")


public class DeleteBookingTests extends BaseTest {


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
    @Description("Delete an existing booking")
    @Step("Delete an existing booking")
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
    @Description("Delete non existent Id booking")
    @Step("Delete non existent Id booking")
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
    @Description("Delete string id booking")
    @Step("Delete string id booking")
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
    @Description("Delete booking without id")
    @Step("Delete booking without id")
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
    @Description("Delete booking without authorisation")
    @Step("Delete booking without authorisation")
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


