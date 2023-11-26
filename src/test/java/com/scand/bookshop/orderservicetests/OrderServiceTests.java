package com.scand.bookshop.orderservicetests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Order;
import com.scand.bookshop.entity.OrderStatus;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.repository.OrderRepository;
import com.scand.bookshop.service.OrderService;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTests extends BaseTest {
    private Book testBook;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RegistrationService registrationService;

    @BeforeAll
    private void createBook() {
        Book book = new Book(null,
                "old title",
                "old genre",
                "old author",
                "old filepath",
                UUID.randomUUID().toString(),
                "description", 6.00);
        testBook = bookRepository.save(book);
    }

    @BeforeAll
    void createAdmin() {
        createAdmin(registrationService, "adminOrderS", "adminOrdS@mail.ru");
    }

    private Order createOrder() {
        User user = userService.findUserByUsername("adminOrderS").get();
        return orderService.createOrder(testBook, user);
    }

    @Test
    public void createOrder_shouldCreateOrder() {
        User user = userService.findUserByUsername("adminOrderS").get();
        Order order = orderService.createOrder(testBook, user);
        assertThat(orderRepository.findByUuid(order.getUuid())).isPresent();
    }

    @Test
    public void changeStatus_shouldSetPaidStatus() {
        User user = userService.findUserByUsername("adminOrderS").get();
        Order order = orderService.createOrder(testBook, user);
        orderService.changeStatus(order, OrderStatus.PAID);
        Order newOrder = orderService.getOrder(order.getUuid());
        assertThat(newOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    public void changeStatus_shouldSetCancelledStatus() {
        Order order = createOrder();
        orderService.changeStatus(order, OrderStatus.CANCELLED);
        assertThat(orderRepository.findByUuid(order.getUuid()).get().getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }
}
