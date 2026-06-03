package com.codingshuttle.librarysystem.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books", indexes = {
        @Index(columnList = "title", name = "idx_title")
})
@ToString(exclude = {"author"})
public class BookEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;

    private String description;

    private String genre;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AuthorEntity author;

    @Column(unique = true)
    private UUID isbn;

    private Double price;

    private LocalDate publishedDate;


}
