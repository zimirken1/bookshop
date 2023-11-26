package com.scand.bookshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "book_id", nullable = false)
    private String uuid;

    @Column(name="description")
    private String description;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @Column(name = "price", nullable = false)
    private double price;

    public Book(Long id,
                String title,
                String genre,
                String author,
                String filePath,
                String uuid,
                String description,
                double price) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.filePath = filePath;
        this.uuid = uuid;
        this.description = description;
        this.price = price;
    }
}