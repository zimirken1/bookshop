package com.scand.bookshop.facade;

import com.scand.bookshop.dto.CartBookResponseDTO;
import com.scand.bookshop.dto.CartRequestDTO;
import com.scand.bookshop.dto.CreateOrderResponseDTO;
import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.entity.Cart;
import com.scand.bookshop.entity.CartItem;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.security.service.UserDetailsImpl;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.CartService;
import com.scand.bookshop.service.OrderService;
import com.scand.bookshop.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartFacade {

  private final CartService cartService;
  private final UserService userService;
  private final BookService bookService;
  private final OrderService orderService;

  public void addItem(CartRequestDTO cartRequestDTO, UserDetailsImpl userDetails) {
    User user = userService.getUserById(userDetails.getId());
    cartService.addItem(cartService.getCart(user),
        bookService.getBookByUuid(cartRequestDTO.getBookUuid()));
  }

  public List<CartBookResponseDTO> getItems(UserDetailsImpl userDetails) {
    User user = userService.getUserById(userDetails.getId());
    List<CartItem> items = cartService.getItems(user);
    return items.stream().map(DTOConverter::toDTO).collect(Collectors.toList());
  }

  public CreateOrderResponseDTO createOrderFromCart(UserDetailsImpl userDetails) {
    User user = userService.getUserById(userDetails.getId());
    List<CartItem> items = cartService.getItems(user);
    CreateOrderResponseDTO responseDTO = DTOConverter.toCreateDTO(orderService.createOrderFromCart(
        items,
        user));
    cartService.clearCart(cartService.getCart(user));
    return responseDTO;
  }

  public void removeItem(CartRequestDTO cartRequestDTO, UserDetailsImpl userDetails) {
    Cart cart = cartService.getCart(userService.getUserById(userDetails.getId()));
    CartItem cartItem = cartService.getCartItem(cart,
        bookService.getBookByUuid(cartRequestDTO.getBookUuid()));
    cartService.removeItem(cart, cartItem);
  }
}
