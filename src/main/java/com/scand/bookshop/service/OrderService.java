package com.scand.bookshop.service;

import com.scand.bookshop.entity.*;
import com.scand.bookshop.repository.OrderDetailRepository;
import com.scand.bookshop.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final OrderDetailRepository orderDetailRepository;
  private final OrderRepository orderRepository;
  private final MessageSource messageSource;
  private final HttpServletRequest request;

  private Order createEmptyOrder(User user) {
    return new Order(null,
        UUID.randomUUID().toString(),
        user,
        LocalDateTime.now(),
        null,
        OrderStatus.PENDING);
  }

  @Transactional
  public Order createOrder(Book book, User user) {
    Order order = createEmptyOrder(user);
    orderRepository.save(order);
    OrderDetail orderDetail = new OrderDetail(null, order, book, null);
    orderDetailRepository.save(orderDetail);
    log.info("Order detail with id '{}' created", orderDetail.getOrderDetailId());
    order.getOrderDetails().add(orderDetail);
    log.info("Order with id '{}' created", order.getId());
    return order;
  }

  @Transactional
  public Order createOrderFromCart(List<CartItem> cartItems, User user) {
    Order order = createEmptyOrder(user);
    orderRepository.save(order);
    cartItems.forEach((item) -> {
      OrderDetail orderDetail = new OrderDetail(null, order, item.getBook(), null);
      orderDetailRepository.save(orderDetail);
      order.getOrderDetails().add(orderDetail);
    });
    return order;
  }

  public Optional<Order> findOrderByUuid(String uuid) {
    return orderRepository.findByUuid(uuid);
  }

  public Order getOrder(String uuid) {
    return findOrderByUuid(uuid)
        .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
            "order_not_found", null, request.getLocale())));
  }

  public Page<Order> getAllOrdersPage(Pageable pageable, User user) {
    return orderRepository.findAllByUser(user, pageable);
  }

  public List<Order> getAllPaidOrders(User user) {
    return orderRepository.findAllByUserAndStatus(user, OrderStatus.PAID);
  }

  @Transactional
  public void changeStatus(Order order, OrderStatus status) {
    order = orderRepository.getReferenceById(order.getId());
    if (order.getStatus() == OrderStatus.PENDING) {
      order.setStatus(status);
      order.getOrderDetails().forEach(detail -> {
        detail.setUnitPrice(BigDecimal.valueOf(detail.getBook().getPrice()));
      });
      BigDecimal totalPrice = order.getOrderDetails().stream()
          .map(OrderDetail::getUnitPrice)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      order.setTotalPrice(totalPrice);
      log.info("Status '{}' set for order '{}'", status.toString(), order.getId());
    } else {
      throw new RuntimeException("Order closed");
    }
  }

  public boolean isBookPaid(Book book, User user) {
    if (user == null) {
      return false;
    }
    AtomicBoolean isPaid = new AtomicBoolean(false);
    List<Order> ordersOfUser = getAllPaidOrders(user);
    ordersOfUser.forEach((order) -> {
      order.getOrderDetails()
          .forEach((orderDetail) -> {
            if (orderDetail.getBook().equals(book)) {
              isPaid.set(true);
            }
          });
    });
    return isPaid.get();
  }
}
