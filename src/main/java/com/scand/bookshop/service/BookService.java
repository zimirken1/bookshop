package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final FileService fileService;
    private final BookCoverService bookCoverService;
    private final MessageSource messageSource;
    private final HttpServletRequest request;

    @Value("${scand.bookshop.outputPath}")
    private String outFolder;

    @Transactional
    public Book createBook(String title, String author, String subject, String extension, byte[] content, double price) {
        String uniqueFilename = UUID.randomUUID().toString();
        String filePath = String.format("%s/%s.%s", outFolder, uniqueFilename, extension);
        Book book = new Book(null, title, subject, author, filePath, uniqueFilename,null, price);
        book = bookRepository.save(book);
        fileService.writeFile(Paths.get(book.getFilePath()), content);
        String coverPath = outFolder + "/covers/" + uniqueFilename + ".png";
        fileService.writeFile(Paths.get(coverPath), bookCoverService.generateCover(content, 0));
        log.info("Book with id '{}' created", book.getId());
        return book;
    }

    public byte[] downloadBook(Book book) {
        log.info("Attempting to download book with UUID '{}'", book.getUuid());
        Path path = Paths.get(book.getFilePath());
        if (!Files.exists(path)) {
            log.error("File of the book with UUID '{}' not found", book.getUuid());
            throw new IllegalArgumentException(messageSource.getMessage(
                "file_not_found", null, request.getLocale()));
        }
        byte[] content = fileService.readFile(book.getFilePath());
        log.info("Successfully downloaded book with UUID '{}'", book.getUuid());
        return content;
    }

    public Resource getCover(Book book) {
        log.info("Attempting to fetch cover for book with UUID '{}'", book.getUuid());
        String coverFilePath = outFolder + "/covers/" + book.getUuid() + ".png";
        Path coverPath = Paths.get(coverFilePath);
        if (!Files.exists(coverPath)) {
            log.error("Cover file for book with UUID '{}' not found", book.getUuid());
            throw new IllegalArgumentException(messageSource.getMessage(
                "file_not_found", null, request.getLocale()));
        }
        log.info("Successfully fetched cover for book with UUID '{}'", book.getUuid());
        return new org.springframework.core.io.PathResource(coverPath);
    }

    public List<String> getPreviewImages(Book book) {
        Path path = Paths.get(book.getFilePath());
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(messageSource.getMessage(
                "file_not_found", null, request.getLocale()));
        }
        List<String> previewImages = new ArrayList<>();
        byte[] fileContent = fileService.readFile(book.getFilePath());
        for (int i = 0; i < 4; i++) {
            String base64Image = Base64.getEncoder().encodeToString(bookCoverService.generateCover(fileContent, i));
            previewImages.add(base64Image);
        }
        return previewImages;
    }

    public Optional<Book> findBookByUuid(String uuid) {
        return bookRepository.findByUuid(uuid);
    }

    public Book getBookByUuid(String uuid) {
        return findBookByUuid(uuid)
            .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                "book_not_found", null, request.getLocale())));
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
        log.info("Book with UUID '{}' deleted", book.getUuid());
    }

    @Transactional
    public Book updateBook(Book book, String title, String genre, String author, String description, double price) {
        book = bookRepository.getReferenceById(book.getId());
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        book.setDescription(description);
        book.setPrice(price);
        log.info("Book with UUID '{}' updated", book.getUuid());
        return book;
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Page<Book> searchBooks(String searchTerm, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrAuthorContainingIgnoreCase(searchTerm, searchTerm, searchTerm, pageable);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}