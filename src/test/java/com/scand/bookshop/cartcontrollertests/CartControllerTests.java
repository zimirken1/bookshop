package com.scand.bookshop.cartcontrollertests;

import static org.assertj.core.api.Assertions.assertThat;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.dto.CartRequestDTO;
import com.scand.bookshop.dto.CreateOrderResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.service.CartService;
import com.scand.bookshop.service.OrderService;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartControllerTests extends BaseTest {

  @Autowired
  private RegistrationService registrationService;

  @BeforeAll
  void createAdmin() {
    createAdmin(registrationService, "adminCart", "adminCart@mail.ru");
  }

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CartService cartService;

  @Autowired
  private OrderService orderService;

  @Autowired
  private UserService userService;

  private String jwtToken;

  @BeforeEach
  private void login() {
    jwtToken = login(testRestTemplate, "adminCart", "admin");
  }

  private Book createBook() {
    Book book = new Book(null,
        "test",
        "test",
        "test",
        "testpath",
        UUID.randomUUID().toString(),
        "desc", 6.0);
    return bookRepository.save(book);
  }

  @Test
  public void addItem() {
    Book book = createBook();
    User user = userService.findUserByUsername("adminCart").get();
    cartService.createCart(user);
    CartRequestDTO cartRequestDTO = new CartRequestDTO(book.getUuid());
    ResponseEntity<String> response = makePostRequestWithToken(jwtToken,
        "/cart/add",
        cartRequestDTO,
        String.class);
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(cartService.getCart(user)
        .getCartItems())
        .isNotEmpty();
  }

  @Test
  public void removeItem() {
    Book book = createBook();
    User user = userService.findUserByUsername("adminCart").get();
    cartService.clearCart(cartService.getCart(user));
    CartRequestDTO cartRequestDTO = new CartRequestDTO(book.getUuid());
    makePostRequestWithToken(jwtToken,
        "/cart/add",
        cartRequestDTO,
        String.class);
    assertThat(cartService.getCart(user)
        .getCartItems())
        .isNotEmpty();
    ResponseEntity<String> response = makePostRequestWithToken(jwtToken,
        "/cart/remove",
        cartRequestDTO,
        String.class);
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(cartService.getCart(user)
        .getCartItems())
        .isEmpty();
  }

  @Test
  public void createOrder() {
    Book book = createBook();
    User user = userService.findUserByUsername("adminCart").get();
    CartRequestDTO cartRequestDTO = new CartRequestDTO(book.getUuid());
    makePostRequestWithToken(jwtToken,
        "/cart/add",
        cartRequestDTO,
        String.class);
    ResponseEntity<CreateOrderResponseDTO> response = makeGetRequestWithToken(jwtToken,
        "/cart/order",
        CreateOrderResponseDTO.class);
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(cartService.getCart(user).getCartItems()).isEmpty();
    assertThat(orderService.getOrder(Objects.requireNonNull(response.getBody()).getUuid())).isNotNull();
  }
}
