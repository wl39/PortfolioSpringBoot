package com.wl39.portfolio.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPatch {
    private Long id;
    private String email; // 로그인용 이메일
    private String username;
    private String role;
}
