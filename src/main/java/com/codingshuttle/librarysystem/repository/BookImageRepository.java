package com.codingshuttle.librarysystem.repository;

import com.codingshuttle.librarysystem.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookImageRepository extends JpaRepository<BookImage, UUID> {
}
