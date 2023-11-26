package com.scand.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponseDTO {
    private String uuid;
    private String username;
    private String orderDate;
    private BigDecimal totalPrice;
    private String status;
    private List<OrderDetailResponseDTO> orderDetails;
}
