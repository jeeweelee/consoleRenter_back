package com.jia.consolerenter.controller;
import com.jia.consolerenter.security.jwt.JwtUtils;


import com.jia.consolerenter.exception.UserAlreadyExistsException;
import com.jia.consolerenter.model.User;
import com.jia.consolerenter.request.LoginRequest;
import com.jia.consolerenter.request.RefreshTokenRequest;
import com.jia.consolerenter.response.JwtResponse;

import com.jia.consolerenter.security.user.StoreUserDetails;
import com.jia.consolerenter.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Add this line

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registration successful!");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser((StoreUserDetails) authentication.getPrincipal());
        String refreshToken = jwtUtils.generateRefreshToken((StoreUserDetails) authentication.getPrincipal());
        StoreUserDetails userDetails = (StoreUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                userDetails.getEmail(),
                jwt, // Ensure this is `accessToken` or adjust to your response structure
                refreshToken,
                roles
        ));
    }




    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtils.key())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
            String email = claims.getSubject();
            User user = userService.getUser(email);
            StoreUserDetails userDetails = StoreUserDetails.buildUserDetails(user);
            String newAccessToken = jwtUtils.generateJwtTokenForUser(userDetails);
            return ResponseEntity.ok(new JwtResponse(
                    userDetails.getId(),
                    userDetails.getEmail(),
                    newAccessToken,
                    refreshToken,
                    userDetails.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList()));
        } catch (Exception e) {
            logger.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired refresh token.");
        }
    }



}
