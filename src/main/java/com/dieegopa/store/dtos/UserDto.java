package com.dieegopa.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
    // @JsonProperty("id")
    // @JsonInclude(JsonInclude.Include.NON_NULL)
    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Long id;
    private String name;
    private String email;
}