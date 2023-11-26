package com.scand.bookshop.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor {
    public static String encryptPassword(String rawPassword) {
        int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength);
        return bCryptPasswordEncoder.encode(rawPassword);
    }
}
