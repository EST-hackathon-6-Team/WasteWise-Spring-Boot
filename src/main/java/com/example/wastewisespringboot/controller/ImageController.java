package com.example.wastewisespringboot.controller;


import com.example.wastewisespringboot.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ImageController {


    private final ImageService imageService;

    @GetMapping("/temp_upload")
    public String uploadImageForm() {
        return "temp_upload";
    }
}
