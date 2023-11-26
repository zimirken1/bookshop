package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Order;
import com.scand.bookshop.entity.OrderStatus;
import com.scand.bookshop.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUuid(String uuid);
    Page<Order> findAllByUser(User user, Pageable pageable);
    List<Order> findAllByUserAndStatus(User user, OrderStatus orderStatus);
}

