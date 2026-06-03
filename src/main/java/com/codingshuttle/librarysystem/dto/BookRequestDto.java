package com.codingshuttle.librarysystem.dto;

import com.codingshuttle.librarysystem.entity.BookEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class BookRequestDto implements Serializable {


    @NotBlank(message = "Title is required : Book title cannot be empty")
    String title;
    @NotBlank(message = "Description is required : Book description cannot be empty")
    String description;
    @NotBlank(message = "Genre is required : Book genre cannot be empty")
    String genre;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID isbn;
    @NotNull(message = "Price is required : Book price cannot be empty")
    Double price;
    @NotNull(message = "Published date is required : Book published date cannot be empty")
    LocalDate publishedDate;
    @NotNull(message = "Author id is required : Book author id cannot be empty")
    Long authorId;

    String imageUrl;



}