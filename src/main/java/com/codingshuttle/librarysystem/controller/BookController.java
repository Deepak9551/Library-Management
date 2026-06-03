package com.codingshuttle.librarysystem.controller;

import com.codingshuttle.librarysystem.dto.BookRequestDto;
import com.codingshuttle.librarysystem.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestPart("book") String book, @RequestPart("image") MultipartFile image) {
        ObjectMapper mapper = new ObjectMapper();

        BookRequestDto bookRequestDto =
                mapper.readValue(book, BookRequestDto.class);
        System.out.println("Read data "+ bookRequestDto);
        return ResponseEntity.ok(bookService.saveBook(bookRequestDto, image));
    }
@GetMapping
    public ResponseEntity<?> getAllBooks(
            @RequestParam(name = "pageNo", defaultValue = "0") String pageNo
        , @RequestParam(name = "pageSize", defaultValue = "5") String pageSize
        , @RequestParam(name = "sort" ,defaultValue = "id") String sort) {
        return ResponseEntity.ok(bookService.getAllBooks(pageNo,pageSize,sort));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.deleteBook(bookId));
    }

    @PatchMapping("/{bookId}")
    public ResponseEntity<?> partialUpdateForBook(@PathVariable Long bookId, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(bookService.partialUpdateBook(bookId, updates));
    }

    @GetMapping("/after-date/{date}")
    public ResponseEntity<?> bookAfterPublisedDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(bookService.bookAfterDate(date));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<?> bookByTitle(@PathVariable String title) {
        return ResponseEntity.ok(bookService.bookByTitle(title));
    }


}