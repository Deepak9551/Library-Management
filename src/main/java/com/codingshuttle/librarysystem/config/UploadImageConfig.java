package com.codingshuttle.librarysystem.config;

import com.codingshuttle.librarysystem.entity.AuthorImage;
import com.codingshuttle.librarysystem.entity.BookImage;
import com.codingshuttle.librarysystem.repository.AuthorImageRepository;
import com.codingshuttle.librarysystem.repository.BookImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class UploadImageConfig {

    private final String IMAGE_URL = "http://localhost:8080/";
    private final AuthorImageRepository authorImageRepository;
    private final BookImageRepository bookImageRepository;

    public UploadImageConfig(AuthorImageRepository authorImageRepository, BookImageRepository bookImageRepository) {
        this.authorImageRepository = authorImageRepository;
        this.bookImageRepository = bookImageRepository;
    }

    public AuthorImage uploadAuthorImage(MultipartFile image, Long authorId){


        AuthorImage authorImage = new AuthorImage();

        authorImage.setFileName(image.getOriginalFilename());
        authorImage.setFileType(image.getContentType());
        authorImage.setImageUrl(IMAGE_URL+authorId);
        authorImage.setFileSize(image.getSize());
        try {
            authorImage.setImage(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        authorImage.setAuthorId(authorId);




        uploadImage(image);
        authorImageRepository.save(authorImage);

        return authorImage;
    }

    public Boolean uploadBookImage(MultipartFile image, Long bookId){


        BookImage bookImage = new BookImage();

        bookImage.setFileName(image.getOriginalFilename());
        bookImage.setFileType(image.getContentType());
        bookImage.setImageUrl(IMAGE_URL+bookId);
        bookImage.setFileSize(image.getSize());
        try {
            bookImage.setImage(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bookImage.setBookId(bookId);
        uploadImage(image);
        bookImageRepository.save(bookImage);

        return true;
    }
    private   void uploadImage(MultipartFile image){

       checkImage(image);
        readImage(image);
        saveImage(image, UUID.randomUUID());


    }

    private void checkImage(MultipartFile image){

        // validate
        // check image type
        // check image size
        // check image format

        if(!image.getOriginalFilename().endsWith("jpg") && !image.getOriginalFilename().endsWith("jpeg") && !image.getOriginalFilename().endsWith("png")){
           throw new RuntimeException("Invalid image type");
        }

        // 5MB
        if(image.getSize() > 5000000){

            throw new RuntimeException("Image size exceeds 5MB");
        }
    }

    private void readImage(MultipartFile file){
        try {
            BufferedImage read = ImageIO.read(file.getInputStream());
            log.info("Image size: {}", read.getWidth() + "x" + read.getHeight());
            log.info("Image type: {}", read.getType());
            log.info("Image color model: {}", read.getColorModel());

            log.info("Image size in bytes: {}", read.getData().getDataBuffer().getSize());
            log.info("Image has alpha channel: {}", read.getAlphaRaster() != null);
            log.info("Image has color model: {}", read.getColorModel().hasAlpha());
            log.info("Image has transparency: {}", read.getTransparency() != Transparency.OPAQUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveImage(MultipartFile file, UUID id){

        File folder = new File("D:/java/coding-shuttle/LibrarySystem/uploads/images");
        folder.mkdirs();

        String ext = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf('.') + 1);

        File image = new File(folder, id + "." + ext);

        try {
            file.transferTo(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
