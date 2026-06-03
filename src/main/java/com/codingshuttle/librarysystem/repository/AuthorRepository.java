package com.codingshuttle.librarysystem.repository;

import com.codingshuttle.librarysystem.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    @Query("select a from AuthorEntity a where a.name=:authodName")
    AuthorEntity findByAuthorName(@Param("authorName") String name);



}