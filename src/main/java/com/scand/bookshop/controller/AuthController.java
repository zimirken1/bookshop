package com.scand.bookshop.controller;

import com.scand.bookshop.dto.JwtDTO;
import com.scand.bookshop.dto.RefreshTokenDTO;
import com.scand.bookshop.dto.UserLoginDTO;
import com.scand.bookshop.dto.UserRegistrationDTO;
import com.scand.bookshop.facade.AuthFacade;
import com.scand.bookshop.security.jwt.JwtResponse;
import com.scand.bookshop.utility.ServerMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthFacade authFacade;

    @PostMapping(value = "/register")
    public ServerMessage register(@RequestBody @Valid UserRegistrationDTO userRegData) {
        return authFacade.register(userRegData);
    }

    @PostMapping(value = "/signin")
    public JwtResponse login(@Valid @RequestBody UserLoginDTO userLoginData) {
        return authFacade.authenticateUser(userLoginData);
    }

    @PostMapping(value = "/refresh")
    public JwtDTO refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        return authFacade.refreshToken(refreshTokenDTO);
    }

    @GetMapping(value = "/activate/{activationCode}")
    public void activateAccount(@PathVariable String activationCode) {
        authFacade.activate(activationCode);
    }
}
