package com.codingshuttle.librarysystem.service;

import com.codingshuttle.librarysystem.dto.AuthorRequestDto;
import com.codingshuttle.librarysystem.dto.BookRequestDto;
import com.codingshuttle.librarysystem.entity.AuthorEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AuthorService {



    AuthorRequestDto createAuthor(AuthorRequestDto authorRequestDto, MultipartFile image);

    List<AuthorRequestDto> getAllAuthors();

    AuthorRequestDto getAuthorById(Long id);

    Boolean deleteAuthor(Long id);

    void existAuthorById(Long id);

    AuthorRequestDto fromEntity(AuthorEntity authorEntity);

    AuthorEntity toEntity(AuthorRequestDto authorRequestDto);

    AuthorRequestDto partialUpdateForAuthor(Long authorId, Map<String, Object> updates);

    List<BookRequestDto> findAllAuthorBooks(Long authodId);
}
