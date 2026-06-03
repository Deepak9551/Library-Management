package com.codingshuttle.librarysystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "authors", indexes = {
        @Index(columnList = "name", name = "idx_name")
})
@ToString(exclude = "books")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;

    private String bio;


    @OneToMany(mappedBy = "author", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)

    private List<BookEntity> books = new ArrayList<>();
}
