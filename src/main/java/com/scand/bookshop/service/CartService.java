package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Cart;
import com.scand.bookshop.entity.CartItem;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.CartItemRepository;
import com.scand.bookshop.repository.CartRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

  private final CartRepository cartRepository;
  private final MessageSource messageSource;
  private final HttpServletRequest request;
  private final CartItemRepository cartItemRepository;

  public void createCart(User user) {
    Cart cart = new Cart(user);
    cartRepository.save(cart);
    log.info("Cart created with id: " + cart.getCartId());
  }

  public void clearCart(Cart cart) {
    cart.getCartItems().clear();
    cartRepository.save(cart);
    log.info("Cart with id {} cleared", cart.getCartId());
  }

  public Optional<Cart> findCartByUser(User user) {
    return cartRepository.findByUser(user);
  }

  public Optional<CartItem> findCartItemByCartAndBook(Cart cart, Book book) {
    return cartItemRepository.findByCartAndBook(cart, book);
  }

  public Cart getCart(User user) {
    return findCartByUser(user)
        .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
            "cart_not_found", null, request.getLocale())));
  }

  public CartItem getCartItem(Cart cart, Book book) {
    return findCartItemByCartAndBook(cart, book)
        .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
            "cart_item_not_found", null, request.getLocale())));
  }

  @Transactional
  public void addItem(Cart cart, Book book) {
    log.info("Trying to add book with uuid {} to cart with id {}: ",
        book.getUuid(),
        cart.getCartId());
    Optional<CartItem> newItem = findCartItemByCartAndBook(cart,book);
    if (newItem.isPresent()) {
      log.warn("Cart item already exists {}", newItem.get().getCartItemId());
      throw new RuntimeException(messageSource.getMessage(
          "cart_item_already_exists", null, request.getLocale()));
    }
    Cart cartRef = cartRepository.getReferenceById(cart.getCartId());
    CartItem cartItem = new CartItem(null, cartRef, book, 1);
    cartRef.getCartItems().add(cartItem);
    cartRepository.save(cartRef);
    log.info("Book added to cart {}", cart.getCartId());
  }

  @Transactional
  public void removeItem(Cart cart, CartItem cartItem) {
    log.info("Trying to remove item {} from cart {}: ", cartItem.getCartItemId(), cart.getCartId());
    cartItem = cartItemRepository.getReferenceById(cartItem.getCartItemId());
    Cart cartRef = cartRepository.getReferenceById(cart.getCartId());
    cartRef.getCartItems().remove(cartItem);
    log.info("Item removed from cart {}", cart.getCartId());
  }

  public List<CartItem> getItems(User user) {
    return getCart(user).getCartItems();
  }
}
