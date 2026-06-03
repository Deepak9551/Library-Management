package com.codingshuttle.librarysystem.service.impl;

import com.codingshuttle.librarysystem.config.UploadImageConfig;
import com.codingshuttle.librarysystem.dto.AuthorRequestDto;
import com.codingshuttle.librarysystem.dto.BookRequestDto;
import com.codingshuttle.librarysystem.entity.AuthorEntity;
import com.codingshuttle.librarysystem.entity.AuthorImage;
import com.codingshuttle.librarysystem.entity.BookEntity;
import com.codingshuttle.librarysystem.exception.ResourceNotFound;
import com.codingshuttle.librarysystem.repository.AuthorRepository;
import com.codingshuttle.librarysystem.service.AuthorService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private UploadImageConfig uploadImageConfig;

    private final AuthorRepository authorRepository;

    private final ModelMapper authorMapper;


    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }


    @Override
    @Transactional
    public AuthorRequestDto createAuthor(AuthorRequestDto authorRequestDto, MultipartFile image) {

        AuthorEntity authorEntity = toEntity(authorRequestDto);

        // Explicitly initialize list to avoid NullPointerException or Detached issues
        authorEntity.setBooks(new ArrayList<>());

        // 1. Handle Multiple Books List
        if (authorRequestDto.getBooks() != null && !authorRequestDto.getBooks().isEmpty()) {
            List<BookEntity> books = extractBooks(authorRequestDto);
            for (BookEntity book : books) {
                book.setAuthor(authorEntity); // 🔴 CRITICAL: Bidirectional relationship fix
                authorEntity.getBooks().add(book);
            }
        }

        // 2. Handle Single Book
        if (authorRequestDto.getBook() != null) {
            BookEntity book = extractBook(authorRequestDto);
            book.setAuthor(authorEntity); // 🔴 CRITICAL: Bidirectional relationship fix
            authorEntity.getBooks().add(book);
        }

        // Save will now cascade properly without "Detached entity" error
        AuthorEntity registeredAuthor = authorRepository.save(authorEntity);

        if (image != null) {
            AuthorImage authorImage = uploadImageConfig.uploadAuthorImage(image, registeredAuthor.getId());
            AuthorRequestDto authorRequestDtowithImage = fromEntity(registeredAuthor);
            authorRequestDtowithImage.setImage(authorImage.getImageUrl());
            return authorRequestDtowithImage;
        }

        return fromEntity(registeredAuthor);
    }

    private List<BookEntity> extractBooks(AuthorRequestDto authorRequestDto) {
        List<BookRequestDto> books = authorRequestDto.getBooks();
        return books.stream()
                .map(bookRequestDto -> {
                    BookEntity book = authorMapper.map(bookRequestDto, BookEntity.class);
                    book.setId(null); // 🔴 FIX: Ensure ID is null so Hibernate treats it as NEW (Not Detached)
                    book.setIsbn(UUID.randomUUID());
                    return book;
                })
                .toList();
    }

    private BookEntity extractBook(AuthorRequestDto authorRequestDto) {
        BookEntity book = authorMapper.map(authorRequestDto.getBook(), BookEntity.class);
        book.setId(null); // 🔴 FIX: Ensure ID is null so Hibernate treats it as NEW (Not Detached)
        book.setIsbn(UUID.randomUUID());
        return book;
    }
    @Override
    public List<AuthorRequestDto> getAllAuthors() {
        List<AuthorEntity> authorEntities = authorRepository.findAll();

        return authorEntities.stream()
                .map(this::fromEntity)
                .toList();
    }

    @Override
    public AuthorRequestDto getAuthorById(Long authorId) {
        existAuthorById(authorId);
        return fromEntity(authorRepository.findById(authorId).get());
    }

    @Override
    public Boolean deleteAuthor(Long authorId) {
        existAuthorById(authorId);
    authorRepository.deleteById(authorId);
        return true;
    }


    @Override
    public void existAuthorById(Long id) {
        authorRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Author not found"));
    }

    @Override
    public AuthorRequestDto fromEntity(AuthorEntity authorEntity) {
        return authorMapper.map(authorEntity, AuthorRequestDto.class);
    }

    @Override
    public AuthorEntity toEntity(AuthorRequestDto authorRequestDto) {
        return authorMapper.map(authorRequestDto, AuthorEntity.class);
    }

    @Override
    public AuthorRequestDto partialUpdateForAuthor(Long authorId, Map<String, Object> updates){
        existAuthorById(authorId);
        AuthorEntity authorEntity = authorRepository.findById(authorId).get();
        updates.forEach((field,value)->{
            Field authorField = ReflectionUtils.findField(AuthorEntity.class, field);
            ReflectionUtils.setField(authorField,authorEntity,value);
        });
        authorRepository.save(authorEntity);
        return fromEntity(authorEntity);
    }

    @Override
    @Transactional
    public List<BookRequestDto> findAllAuthorBooks(Long authodId){
        existAuthorById(authodId);
        AuthorEntity authorEntity = authorRepository.findById(authodId).get();
        return authorEntity.getBooks()
                .stream()
                .map(bookEntity -> fromEntity(bookEntity, BookRequestDto.class))
                .toList();
    }

    private BookRequestDto fromEntity(Object source, Class<BookRequestDto> destinationType) {
        return authorMapper.map(source, destinationType);
    }





}
