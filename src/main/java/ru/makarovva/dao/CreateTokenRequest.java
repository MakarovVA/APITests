package ru.makarovva.dao;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "password"
})
@Builder
@Data

public class CreateTokenRequest {

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

}
