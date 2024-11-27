package com.trollyworld.bootapp.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Long userId;
    private String jwtToken;
    private List<String> roles;
    private String userName;

    public UserInfoResponse(Long userId, String userName, List<String> roles) {
        this.userId=userId;
        this.userName=userName;
        this.roles=roles;
    }
}
