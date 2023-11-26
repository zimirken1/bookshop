package com.scand.bookshop.facade;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.dto.PageResponseDTO;
import com.scand.bookshop.dto.PriceDTO;
import com.scand.bookshop.dto.RatingRequestDTO;
import com.scand.bookshop.dto.RatingResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.security.service.UserDetailsImpl;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.CommentService;
import com.scand.bookshop.service.EmailService;
import com.scand.bookshop.service.FileService;
import com.scand.bookshop.service.OrderService;
import com.scand.bookshop.service.RatingService;
import com.scand.bookshop.service.UserService;
import com.scand.bookshop.service.metadataextractor.Extractor;
import com.scand.bookshop.service.metadataextractor.Metadata;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class BookFacade {

  private final BookService bookService;
  private final List<Extractor> extractors;
  private final FileService fileService;
  private final MessageSource messageSource;
  private final HttpServletRequest request;
  private final UserService userService;
  private final RatingService ratingService;
  private final CommentService commentService;
  private final OrderService orderService;
  private final EmailService emailService;

  public BookResponseDTO uploadBook(MultipartFile file, PriceDTO priceDTO) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException(messageSource.getMessage("file_empty",
          null,
          request.getLocale()));
    }
    String extension = fileService.getExtension(file);
    Extractor extractor = extractors.stream()
        .filter(fileExtractor -> fileExtractor.getExtension().equals(extension))
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException(messageSource.getMessage(
                "extractor_not_found",
                null,
                request.getLocale())));
    Metadata metadata = extractor.extractMetaData(file);
    Book book = bookService.createBook(metadata.getTitle(),
        metadata.getAuthor(),
        metadata.getSubject(),
        extension,
        metadata.getContent(),
        priceDTO.getPrice());
    return DTOConverter.toDTO(book, null);
  }

  public PageResponseDTO getBooksPage(int pageNumber,
                                      int pageSize,
                                      String sortField,
                                      String sortDirection,
                                      String searchTerm,
                                      UserDetailsImpl userDetails) {
    Sort.Direction direction = Sort.Direction.valueOf(sortDirection.toUpperCase());
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));
    Page<Book> bookPage;
    Optional<User> userOptional = Optional.ofNullable(userDetails)
        .map(UserDetailsImpl::getId)
        .map(userService::getUserById);

    if (searchTerm == null) {
      bookPage = bookService.getAllBooks(pageable);
    } else {
      bookPage = bookService.searchBooks(searchTerm, pageable);
    }

    List<BookResponseDTO> bookList = bookPage.getContent().stream()
        .map(book -> DTOConverter.toDTO(book,
            userOptional.map(u -> orderService.isBookPaid(book, u))
                .orElse(false)))
        .collect(Collectors.toList());

    int totalPages = bookPage.getTotalPages();
    return new PageResponseDTO(bookList, totalPages);
  }

  public List<BookResponseDTO> getAllBooks() {
    return bookService.getAllBooks().stream()
        .map((book) -> DTOConverter.toDTO(book, null))
        .collect(Collectors.toList());
  }

  public BookResponseDTO getBook(String uuid, UserDetailsImpl userDetails) {
    Optional<User> userOptional = Optional.ofNullable(userDetails)
        .map(UserDetailsImpl::getId)
        .map(userService::getUserById);
    Book book = bookService.getBookByUuid(uuid);
    return DTOConverter.toDTO(book, userOptional.map(u -> orderService.isBookPaid(book, u))
        .orElse(false));
  }

  public Resource getBookCover(String uuid) {
    Book book = bookService.getBookByUuid(uuid);
    return bookService.getCover(book);
  }

  public List<String> getPreviewImages(String uuid) {
    Book book = bookService.getBookByUuid(uuid);
    return bookService.getPreviewImages(book);
  }

  public void deleteBook(String uuid) {
    Book book = bookService.getBookByUuid(uuid);
    commentService.clearComments(commentService.getComments(book));
    bookService.deleteBook(book);
  }

  public BookResponseDTO updateBook(String uuid, BookRequestDTO updatedBook) {
    Book book = bookService.getBookByUuid(uuid);
    book = bookService.updateBook(book,
        updatedBook.getTitle(),
        updatedBook.getGenre(),
        updatedBook.getAuthor(),
        updatedBook.getDescription(),
        updatedBook.getPrice()
    );
    return DTOConverter.toDTO(book, null);
  }

  public ResponseEntity<byte[]> downloadBook(String uuid) {
    Book book = bookService.getBookByUuid(uuid);
    byte[] content = bookService.downloadBook(book);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "book.pdf");
    return new ResponseEntity<>(content, headers, HttpStatus.OK);
  }

  public void addRating(String uuid, RatingRequestDTO ratingRequestDTO,
                        UserDetailsImpl userPrincipal) {
    Book book = bookService.getBookByUuid(uuid);
    User user = userService.getUserById(userPrincipal.getId());
    ratingService.addRating(book, user, ratingRequestDTO.getRatingValue());
  }

  public RatingResponseDTO getRating(String uuid) {
    Book book = bookService.getBookByUuid(uuid);
    return DTOConverter.toDTO(ratingService.calculateAverageRating(book));
  }
}
