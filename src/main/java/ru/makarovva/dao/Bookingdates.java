package ru.makarovva.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "checkin",
        "checkout"
})
@Builder
@Data
public class Bookingdates {

    @JsonProperty("checkin")
    public String checkin;
    @JsonProperty("checkout")
    public String checkout;

}