package com.scand.bookshop.controller;

import com.scand.bookshop.dto.CartBookResponseDTO;
import com.scand.bookshop.dto.CartRequestDTO;
import com.scand.bookshop.dto.CreateOrderResponseDTO;
import com.scand.bookshop.facade.CartFacade;
import com.scand.bookshop.security.service.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("cart")
public class CartController {

  private final CartFacade cartFacade;

  @PostMapping("/add")
  public void addItem(@RequestBody CartRequestDTO cartRequestDTO,
                      @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
    cartFacade.addItem(cartRequestDTO, userPrincipal);
  }

  @GetMapping("/list")
  public List<CartBookResponseDTO> getItems(@AuthenticationPrincipal UserDetailsImpl userPrincipal) {
    return cartFacade.getItems(userPrincipal);
  }

  @GetMapping("/order")
  public CreateOrderResponseDTO createOrder(@AuthenticationPrincipal UserDetailsImpl userPrincipal) {
    return cartFacade.createOrderFromCart(userPrincipal);
  }

  @PostMapping("/remove")
  public void removeItem(@RequestBody CartRequestDTO cartRequestDTO,
                      @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
    cartFacade.removeItem(cartRequestDTO, userPrincipal);
  }
}
