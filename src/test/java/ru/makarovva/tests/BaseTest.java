package ru.makarovva.tests;

import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
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


public abstract class BaseTest {


    static String token;
    String id;
    static Properties properties = new Properties();
    static String baseUrl;


    @BeforeAll
    static void beforeAll() throws IOException {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());

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


}
