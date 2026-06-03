package com.codingshuttle.librarysystem.service.impl;

import com.codingshuttle.librarysystem.config.UploadImageConfig;
import com.codingshuttle.librarysystem.dto.BookRequestDto;
import com.codingshuttle.librarysystem.entity.AuthorEntity;
import com.codingshuttle.librarysystem.entity.BookEntity;
import com.codingshuttle.librarysystem.exception.ResourceNotFound;
import com.codingshuttle.librarysystem.repository.AuthorRepository;
import com.codingshuttle.librarysystem.repository.BookRepository;
import com.codingshuttle.librarysystem.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final ModelMapper bookMapper;

    @Autowired
    private  UploadImageConfig uploadImageConfig;
    public BookServiceImp(BookRepository bookRepository , ModelMapper bookMapper , AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookRequestDto saveBook(BookRequestDto bookRequestDto , MultipartFile image) {

        BookEntity bookEntity = toEntity(bookRequestDto);
        AuthorEntity authorEntity = authorRepository.findById(bookRequestDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFound("Author not found"));
        bookEntity.setAuthor(authorEntity);
        bookEntity.setIsbn(UUID.randomUUID());
        System.out.println("book entity "+bookEntity);
        BookEntity registeredBook = bookRepository.save(bookEntity);
        uploadImageConfig.uploadBookImage(image,registeredBook.getId());
        return fromEntity(bookEntity);


    }

    @Override
    public List<BookRequestDto> getAllBooks(String pageNo, String pageSize, String sort) {
       var pageRequest = PageRequest.of(Integer.parseInt(pageNo), Integer.parseInt(pageSize), Sort.by(Sort.Order.asc(sort),Sort.Order.desc(sort)));
        Page<BookEntity> page = bookRepository.findAll(pageRequest);
       return page.getContent().stream()
                .map(this::fromEntity)
                .toList();

    }

    @Override
    public BookRequestDto getBookById(Long bookId) {

        existBookById(bookId);
        BookEntity bookEntity = bookRepository.findById(bookId).get();
        return fromEntity(bookEntity);
    }

    @Override
    public Boolean deleteBook(Long bookId) {
    existBookById(bookId);
    bookRepository.deleteById(bookId);
        return true;
    }
    @Override
    public BookRequestDto updateBook(Long bookId, BookRequestDto bookRequestDto) {

        existBookById(bookId);
        BookEntity bookEntity = bookRepository.save(toEntity(bookRequestDto));
        return fromEntity(bookEntity);
    }


    public void existBookById(Long bookId) {
        bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFound("Book not found"));
    }

    @Override
    public BookRequestDto partialUpdateBook(Long bookId, Map<String, Object> updates) {
        existBookById(bookId);
        BookEntity bookEntity = bookRepository.findById(bookId).get();
       updates.forEach((key, value) -> {
           // find book field by name
           Field bookField = ReflectionUtils.findField(BookEntity.class, key);
           // set the value of the book field in the persistent book object
           ReflectionUtils.setField(bookField, bookEntity, value);
       });
        BookEntity updatedBookEntity = bookRepository.save(bookEntity);
        return fromEntity(updatedBookEntity);
    }
    @Override
    public BookEntity toEntity(BookRequestDto bookRequestDto){
        return bookMapper.map(bookRequestDto, BookEntity.class);
    }

    @Override
    public BookRequestDto fromEntity(BookEntity bookEntity){
        return bookMapper.map(bookEntity, BookRequestDto.class);
    }

    @Override
    public BookRequestDto bookByTitle(String title){
        BookEntity bookEntity = bookRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFound("Book Not Found"));
        return fromEntity(bookEntity);
    }

    @Override
    public BookRequestDto bookAfterDate(LocalDate date){

        return fromEntity(bookRepository.findByPublishedDateAfter(date));
    }


}
