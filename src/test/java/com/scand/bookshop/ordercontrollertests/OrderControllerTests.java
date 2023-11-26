package com.scand.bookshop.ordercontrollertests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.dto.OrderRequestDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Order;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.service.OrderService;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerTests extends BaseTest {
    private String jwtToken;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeAll
    void createAdmin() {
        createAdmin(registrationService, "adminOrder", "adminOrd@mail.ru");
    }

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void authenticate() {
        jwtToken = login(testRestTemplate, "adminOrder", "admin");
    }

    private Book createBook() {
        Book book = new Book(null,
                "old title",
                "old genre",
                "old author",
                "old filepath",
                UUID.randomUUID().toString(),
                "description", 6.00);
        return bookRepository.save(book);
    }

    @Test
    public void createOrder_shouldCreateOrder() {
        Book book = createBook();
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(book.getUuid());
        ResponseEntity<String> response =
                makePostRequestWithToken(jwtToken, "/order/create", orderRequestDTO, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    public void make100RequestsToChangeStatus(String statusUrlPart) throws InterruptedException {
        Book book = createBook();
        Order order = orderService.createOrder(book, userService.findUserByUsername("adminOrder").get());
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger failCounter = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    ResponseEntity<String> response =
                            makeGetRequestWithToken(jwtToken, "/order/" + order.getUuid() + statusUrlPart, String.class);
                    if (requestCount.incrementAndGet() == 1) {
                        successCounter.getAndIncrement();
                        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
                    } else {
                        failCounter.getAndIncrement();
                        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        assertThat(requestCount.get()).isEqualTo(100);
        assertThat(successCounter.get()).isEqualTo(1);
        assertThat(failCounter.get()).isEqualTo(99);
    }

    @Test
    public void make100RequestsToPay() throws InterruptedException {
        make100RequestsToChangeStatus("/pay");
    }

    @Test
    public void make100RequestsToCancel() throws InterruptedException {
        make100RequestsToChangeStatus("/cancel");
    }
}
