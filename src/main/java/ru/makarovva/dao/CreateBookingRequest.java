package ru.makarovva.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "firstname",
        "lastname",
        "totalprice",
        "depositpaid",
        "bookingdates",
        "additionalneeds"
})
@Builder
@Data
public class CreateBookingRequest {

    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("totalprice")
    private Integer totalprice;
    @JsonProperty("depositpaid")
    private Boolean depositpaid;
    @JsonProperty("bookingdates")
    private Bookingdates bookingdates;
    @JsonProperty("additionalneeds")
    private String additionalneeds;

}


/*
{
     "firstname" : "Jim",
    "lastname" : "Brown",
     "totalprice" : 111,
     "depositpaid" : true,
     "bookingdates" : {
        "checkin" : "2018-01-01",
         "checkout" : "2019-01-01"
     },
     "additionalneeds" : "Breakfast"
 }
 */