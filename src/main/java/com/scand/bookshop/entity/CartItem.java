package com.scand.bookshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_item_id", nullable = false)
  private Long cartItemId;

  @ManyToOne
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @Column(name = "quantity", nullable = false)
  private int quantity;

}
