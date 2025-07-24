package com.wl39.portfolio.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AuthCheckResponse {
    private String username;
    private List<String> roles;
}
