package com.example.wastewisespringboot.controller;

import com.example.wastewisespringboot.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ImageUploadController {

    private final ImageUploadService imageUploadService;
    private final RestTemplate restTemplate;

    private String processedImageUrl;
    private String processedRegion;
    private String processedClassification;

    @Autowired
    public ImageUploadController(ImageUploadService imageUploadService, RestTemplate restTemplate) {
        this.imageUploadService = imageUploadService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("image") MultipartFile image, @RequestParam("region") String region, Model model) throws IOException {
        // 이미지 업로드 로직
        String imageUrl = imageUploadService.upload(image);
        System.out.println("이미지 업로드 완료: " + imageUrl);

        System.out.println("FastAPI 서버로 데이터 전송 시작");
        // FastAPI 서버로 데이터 전송
        String fastApiUrl = "http://127.0.0.1:8000/imageregion";
        Map<String, String> requestData = new HashMap<>();
        requestData.put("imageUrl", imageUrl);
        requestData.put("region", region);

        restTemplate.postForObject(fastApiUrl, requestData, String.class);

        // 모델에 이미지 URL 추가
        model.addAttribute("imageUrl", imageUrl);
        return "image"; //
    }

    @PostMapping("/processed")
    public void processData(@RequestBody Map<String, String> data) {
        // FastAPI 서버로부터 받은 데이터를 처리
        processedImageUrl = data.get("imageUrl");
        processedRegion = data.get("region");
        processedClassification = data.get("classification");
    }

    @GetMapping("/processed")
    public String getProcessedData(Model model) {
        // 모델에 이미지 URL, 지역 정보 및 분류 결과 추가
        model.addAttribute("imageUrl", processedImageUrl);
        model.addAttribute("region", processedRegion);
        model.addAttribute("classification", processedClassification);
        return "processed"; // 처리된 이미지를 표시할 페이지로 이동
    }
}
