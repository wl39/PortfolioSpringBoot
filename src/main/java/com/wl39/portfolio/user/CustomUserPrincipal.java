package com.wl39.portfolio.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class CustomUserPrincipal {
    private String email;
    private String username;
}
