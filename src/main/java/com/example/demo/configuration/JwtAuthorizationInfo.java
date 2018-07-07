package com.example.demo.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtAuthorizationInfo {

    @JsonProperty("name")
    private String name;

    @JsonProperty("organization")
    private String organization;
}
