package com.codingshuttle.librarysystem.service;

import com.codingshuttle.librarysystem.dto.BookRequestDto;
import com.codingshuttle.librarysystem.entity.BookEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookService {

    BookRequestDto saveBook(BookRequestDto bookRequestDto , MultipartFile image);

    List<BookRequestDto> getAllBooks(String pageNo, String pageSize, String sort);

    BookRequestDto getBookById(Long bookId);

    Boolean deleteBook(Long bookId);

    BookRequestDto updateBook(Long bookId, BookRequestDto bookRequestDto);

    BookRequestDto partialUpdateBook(Long bookId, Map<String, Object> updates);

    BookEntity toEntity(BookRequestDto bookRequestDto);

    BookRequestDto fromEntity(BookEntity bookEntity);

    BookRequestDto bookByTitle(String title);

    BookRequestDto bookAfterDate(LocalDate date);
}
