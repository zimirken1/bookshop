package com.scand.bookshop.cartservicetests;

import static org.assertj.core.api.Assertions.assertThat;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Cart;
import com.scand.bookshop.entity.CartItem;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.repository.CartItemRepository;
import com.scand.bookshop.service.CartService;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartServiceTests extends BaseTest {

  @Autowired
  private UserService userService;

  @Autowired
  private CartService cartService;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private RegistrationService registrationService;

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

  @BeforeAll
  void createAdmin() {
    createAdmin(registrationService, "adminCartS", "adminCartS@mail.ru");
  }

  @Test
  public void createCart() {
    createAdmin(registrationService, "createTest", "create@mail.ru");
    User user = userService.findUserByUsername("createTest").get();
    cartService.createCart(user);
    assertThat(cartService.getCart(user)).isNotNull();
  }

  @Test
  public void addItem() {
    Book book = createBook();
    User user = userService.findUserByUsername("adminCartS").get();
    cartService.createCart(user);
    Cart cart = cartService.getCart(user);
    cartService.addItem(cart, book);
    assertThat(cartService.getCart(user).getCartItems()).isNotEmpty();
  }

  @Test
  public void removeItem() {
    Book book = createBook();
    createAdmin(registrationService, "removeTest", "remove@mail.ru");
    User user = userService.findUserByUsername("removeTest").get();
    cartService.createCart(user);
    Cart cart = cartService.getCart(user);
    assertThat(cartService.getCart(user).getCartItems()).isEmpty();
    cartService.addItem(cart, book);
    assertThat(cart.getCartItems()).isNotEmpty();
    CartItem item = cartItemRepository.findByCartAndBook(cart, book).get();
    cartService.removeItem(cart, item);
    cart = cartService.getCart(user);
    assertThat(cart.getCartItems()).isEmpty();
  }
}
