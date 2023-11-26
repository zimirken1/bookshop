package com.scand.bookshop;

import com.scand.bookshop.dto.UserLoginDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.service.PasswordEncryptor;
import com.scand.bookshop.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;


public class BaseTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    public static void createAdmin(RegistrationService registrationService, String username, String email) {
        registrationService.register(username,
                PasswordEncryptor.encryptPassword("admin"),
                email,
                LocalDateTime.now(),
                User.Role.ADMIN);
    }

    public static String login(TestRestTemplate testRestTemplate, String login, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserLoginDTO userData = new UserLoginDTO(login, password);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<UserLoginDTO> entity = new HttpEntity<UserLoginDTO>(userData, headers);
        ResponseEntity<Map> response = testRestTemplate.postForEntity("/auth/signin", entity, Map.class);
        return Objects.requireNonNull(response.getBody()).get("accessToken").toString();
    }

    private HttpHeaders creatTokenHeaders(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        return headers;
    }

    public <T, U> ResponseEntity<U> makePostRequestWithToken(String jwtToken, String url, T requestDto, Class<U> responseType) {
        HttpHeaders headers = creatTokenHeaders(jwtToken);
        HttpEntity<T> requestEntity = new HttpEntity<>(requestDto, headers);
        return testRestTemplate.postForEntity(url, requestEntity, responseType);
    }

    public <T, U> ResponseEntity<U> makeGetRequestWithToken(String jwtToken, String url, Class<U> responseType) {
        HttpHeaders headers = creatTokenHeaders(jwtToken);
        HttpEntity<T> requestEntity = new HttpEntity<>(headers);
        return testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
    }

    public <T, U> ResponseEntity<U> makeDeleteRequestWithToken(String jwtToken, String url, Class<U> responseType) {
        HttpHeaders headers = creatTokenHeaders(jwtToken);
        HttpEntity<T> requestEntity = new HttpEntity<>(headers);
        return testRestTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType);
    }

    public <T, U> ResponseEntity<U> makePostRequestWithFile(String url,
                                                            HttpEntity<MultiValueMap<String, Object>> requestEntity,
                                                            Class<U> responseType) {
        return testRestTemplate.postForEntity(url, requestEntity, responseType);
    }

    public HttpEntity<MultiValueMap<String, Object>> createEntityWithFile(String path, String jwtToken, String keyWord) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        if (!path.equals("")) {
            FileSystemResource resource = new FileSystemResource(new File(path));
            body.add(keyWord, resource);
        }
        return new HttpEntity<>(body, headers);
    }


}
