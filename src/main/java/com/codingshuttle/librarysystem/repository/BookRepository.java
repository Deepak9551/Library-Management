package com.codingshuttle.librarysystem.repository;

import com.codingshuttle.librarysystem.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findByTitle(String title);

    BookEntity findByPublishedDateAfter(LocalDate publishedDateAfter);

}