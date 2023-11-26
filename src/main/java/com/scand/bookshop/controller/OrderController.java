package com.scand.bookshop.controller;

import com.scand.bookshop.dto.CreateOrderResponseDTO;
import com.scand.bookshop.dto.OrderPageResponseDTO;
import com.scand.bookshop.dto.OrderRequestDTO;
import com.scand.bookshop.dto.OrderResponseDTO;
import com.scand.bookshop.facade.OrderFacade;
import com.scand.bookshop.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("order")
public class OrderController {
    private final OrderFacade orderFacade;

    @PostMapping(value = "/create")
    public CreateOrderResponseDTO createOrder(@Valid @RequestBody OrderRequestDTO orderData,
                                              @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return orderFacade.createOrder(orderData, userPrincipal);
    }

    @GetMapping("/{uuid}")
    public OrderResponseDTO getOrder(@PathVariable String uuid) {
        return orderFacade.getOrder(uuid);
    }

    @GetMapping("/{uuid}/pay")
    public void payOrder(@PathVariable String uuid) {
        orderFacade.payOrder(uuid);
    }

    @GetMapping("/{uuid}/cancel")
    public void cancelOrder(@PathVariable String uuid) {
        orderFacade.cancelOrder(uuid);
    }

    @GetMapping("/history")
    public OrderPageResponseDTO getHistoryPage(@RequestParam int page,
                                               @RequestParam int size,
                                               @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return orderFacade.getOrderHistoryPage(page, size, userPrincipal);
    }
}
