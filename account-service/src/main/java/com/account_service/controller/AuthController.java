package com.account_service.controller;


import com.account_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "Api for login management")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Enpoint for managing login users", description = "login users for get token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login Successfully"),
            @ApiResponse(responseCode = "500", description = "Invalid Credentials")
    })
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String pwd) {
        return authService.login(username, pwd);
    }
}

