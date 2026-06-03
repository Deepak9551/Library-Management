package com.codingshuttle.librarysystem.dto;

import com.codingshuttle.librarysystem.entity.AuthorEntity;
import com.codingshuttle.librarysystem.validation.Phone;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorRequestDto implements Serializable {
    @NotBlank(message = "Name is required : Author name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name;
    @Email(message = "Email is required : Author email cannot be empty")

    String email;
    @NotBlank(message = "Address is required : Author address cannot be empty")
    String address;
    @NotBlank(message = "Phone is required : Author phone cannot be empty")
    @Phone(message = "Invalid phone number")
    String phone;
    String image;
    String bio;

    BookRequestDto book;

    List<BookRequestDto> books;
}