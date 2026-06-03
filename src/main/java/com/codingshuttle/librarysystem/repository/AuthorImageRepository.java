package com.codingshuttle.librarysystem.repository;

import com.codingshuttle.librarysystem.entity.AuthorImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorImageRepository extends JpaRepository< AuthorImage, UUID> {


}
