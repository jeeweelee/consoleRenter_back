package com.jia.consolerenter.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data

@NoArgsConstructor
public class JwtResponse {
    private Long id;
    private String email;
    private String accessToken;
    private String refreshToken;
    private List<String> roles;

    public JwtResponse(Long id, String email, String accessToken, String refreshToken, List<String> roles) {
        this.id = id;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }

    // Getters and setters (or use Lombok annotations)
}



