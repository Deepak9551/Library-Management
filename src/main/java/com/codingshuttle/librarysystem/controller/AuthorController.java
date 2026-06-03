package com.codingshuttle.librarysystem.controller;

import com.codingshuttle.librarysystem.advice.ApiResponse;
import com.codingshuttle.librarysystem.config.UploadImageConfig;
import com.codingshuttle.librarysystem.dto.AuthorRequestDto;
import com.codingshuttle.librarysystem.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private AuthorService authorService;



    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAuthor(@RequestPart("author") String authorJson , @RequestPart("image") @Valid MultipartFile image) {
        ObjectMapper mapper = new ObjectMapper();

        AuthorRequestDto authorRequestDto =
                mapper.readValue(authorJson, AuthorRequestDto.class);
        System.out.println("Read data "+ authorRequestDto);
        return  ResponseEntity.ok(authorService.createAuthor(authorRequestDto,image));
    }

    public ResponseEntity<?> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.deleteAuthor(authorId));
    }

    @PatchMapping("/{authorId}")
    public ResponseEntity<?> partialUpdateForAuthor(@PathVariable Long authorId, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(authorService.partialUpdateForAuthor(authorId, updates));
    }

    @GetMapping("/{authorId}/books")
    public ResponseEntity<?> findAllAuthorBooks(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.findAllAuthorBooks(authorId));
    }

}
