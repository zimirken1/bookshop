package com.scand.bookshop.dto;

import com.scand.bookshop.entity.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DTOConverter {

  public static BookResponseDTO toDTO(Book book, Boolean isPaid) {
    BookResponseDTO responseDTO = new BookResponseDTO();
    responseDTO.setTitle(book.getTitle());
    responseDTO.setGenre(book.getGenre());
    responseDTO.setAuthor(book.getAuthor());
    responseDTO.setUuid(book.getUuid());
    responseDTO.setDescription(book.getDescription());
    responseDTO.setPrice(book.getPrice());
    responseDTO.setIsPaid(isPaid);
    return responseDTO;
  }

  public static AdminPanelUserResponseDTO toDTO(User user) {
    return new AdminPanelUserResponseDTO(
        user.getUuid().toString(),
        user.getLogin(),
        user.getEmail(),
        user.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
        user.getRole()
    );
  }

  public static UserResponseDTO toUserDTO(User user) {
    return new UserResponseDTO(user.getLogin(),
        user.getEmail(),
        user.getRegistrationDate().toLocalDate().toString());
  }

  public static CommentResponseDTO toDTO(Comment comment) {
    CommentResponseDTO responseDTO;
    if (!comment.isRemoved()) {
      responseDTO = new CommentResponseDTO();
      responseDTO.setText(comment.getText());
      responseDTO.setUsername(comment.getUser().getLogin());
      responseDTO.setTimestamp(comment.getTimestamp()
          .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
      responseDTO.setUuid(comment.getUuid());
      responseDTO.setReplies(comment.getReplies()
          .stream()
          .map(DTOConverter::toDTO)
          .collect(Collectors.toList()));
      responseDTO.setRemoved(comment.isRemoved());
    } else {
      responseDTO = new CommentResponseDTO(
          null,
          null,
          null,
          null,
          null,
          comment.isRemoved());
      responseDTO.setReplies(comment.getReplies()
          .stream()
          .map(DTOConverter::toDTO)
          .collect(Collectors.toList()));
    }
    return responseDTO;
  }

  private static OrderDetailResponseDTO toDTO(OrderDetail orderDetail) {
    return new OrderDetailResponseDTO(orderDetail.getBook().getUuid(), orderDetail.getUnitPrice());
  }

  public static OrderResponseDTO toDTO(Order order) {
    return new OrderResponseDTO(order.getUuid(),
        order.getUser().getLogin(),
        order.getOrderDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
        order.getTotalPrice(),
        order.getStatus().toString(),
        order.getOrderDetails().stream()
            .map(DTOConverter::toDTO)
            .collect(Collectors.toList()));
  }

  public static CreateOrderResponseDTO toCreateDTO(Order order) {
    return new CreateOrderResponseDTO(order.getUuid());
  }

  public static CartBookResponseDTO toDTO(CartItem cartItem) {
    CartBookResponseDTO cartBookResponseDTO = new CartBookResponseDTO();
    cartBookResponseDTO.setPrice(cartItem.getBook().getPrice());
    cartBookResponseDTO.setUuid(cartItem.getBook().getUuid());
    return cartBookResponseDTO;
  }

  public static RatingResponseDTO toDTO(double rating) {
    return new RatingResponseDTO(rating);
  }
}
