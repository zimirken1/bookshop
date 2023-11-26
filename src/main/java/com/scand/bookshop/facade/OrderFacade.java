package com.scand.bookshop.facade;

import com.scand.bookshop.dto.*;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Order;
import com.scand.bookshop.entity.OrderStatus;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.security.service.UserDetailsImpl;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.OrderService;
import com.scand.bookshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final BookService bookService;
    private final UserService userService;

    public CreateOrderResponseDTO createOrder(OrderRequestDTO orderData, UserDetailsImpl userDetails) {
        Book book = bookService.getBookByUuid(orderData.getUuid());
        User user = userService.getUserById(userDetails.getId());
        return DTOConverter.toCreateDTO(orderService.createOrder(book, user));
    }

    public OrderResponseDTO getOrder(String uuid) {
        return DTOConverter.toDTO(orderService.getOrder(uuid));
    }

    public void payOrder(String uuid) {
        Order order = orderService.getOrder(uuid);
        orderService.changeStatus(order, OrderStatus.PAID);
    }

    public void cancelOrder(String uuid) {
        Order order = orderService.getOrder(uuid);
        orderService.changeStatus(order, OrderStatus.CANCELLED);
    }

    public OrderPageResponseDTO getOrderHistoryPage(int pageNumber, int pageSize, UserDetailsImpl userDetails) {
        User user = userService.getUserById(userDetails.getId());
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> orderPage = orderService.getAllOrdersPage(pageable, user);
        int totalPages = orderPage.getTotalPages();
        return new OrderPageResponseDTO(orderPage.map(DTOConverter::toDTO).getContent(), totalPages);
    }
}
