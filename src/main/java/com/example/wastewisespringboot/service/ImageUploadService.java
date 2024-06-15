package com.example.wastewisespringboot.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ImageUploadService {

    private final AmazonS3Client s3Client;

    @Autowired
    public ImageUploadService(AmazonS3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${s3.bucket}")
    private String bucket;

    public String upload(MultipartFile image) throws IOException {
        // 파일 이름 변경
        String originalFileName = image.getOriginalFilename();
        String fileName = changeFileName(originalFileName);

        // 파일 메타데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        // S3에 파일 업로드
        s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);

        // 업로드한 파일의 S3 URL 반환
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private String changeFileName(String originalFileName) {
        // 파일 이름 변경 로직
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return originalFileName + "_" + LocalDateTime.now().format(formatter);
    }
}



















//
//package com.example.wastewisespringboot.service;
//
//        import com.amazonaws.services.s3.AmazonS3Client;
//        import com.amazonaws.services.s3.model.ObjectMetadata;
//        import net.coobird.thumbnailator.Thumbnails;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.beans.factory.annotation.Value;
//        import org.springframework.stereotype.Service;
//        import org.springframework.web.multipart.MultipartFile;
//
//        import javax.imageio.ImageIO;
//        import java.awt.image.BufferedImage;
//        import java.io.ByteArrayInputStream;
//        import java.io.ByteArrayOutputStream;
//        import java.io.IOException;
//        import java.time.LocalDateTime;
//        import java.time.format.DateTimeFormatter;
//
//@Service
//public class ImageUploadService {
//
//    private final AmazonS3Client s3Client;
//
//    @Autowired
//    public ImageUploadService(AmazonS3Client s3Client) {
//        this.s3Client = s3Client;
//    }
//
//    @Value("${s3.bucket}")
//    private String bucket;
//
//    public String upload(MultipartFile image) throws IOException {
//        // 이미지를 리사이즈하고 그레이스케일로 변환
//        byte[] processedImage = processImage(image);
//
//        // 업로드할 파일의 이름을 변경
//        String originalFileName = image.getOriginalFilename();
//        String fileName = changeFileName(originalFileName);
//
//        // S3에 업로드할 파일의 메타데이터 생성
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType("image/jpeg");
//        metadata.setContentLength(processedImage.length);
//
//        // S3에 파일 업로드
//        s3Client.putObject(bucket, fileName, new ByteArrayInputStream(processedImage), metadata);
//
//        // 업로드한 파일의 S3 URL 주소 반환
//        return s3Client.getUrl(bucket, fileName).toString();
//    }
//
//    private byte[] processImage(MultipartFile image) throws IOException {
//        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
//
//        // 이미지 리사이즈 및 그레이스케일 변환
//        BufferedImage resizedImage = Thumbnails.of(bufferedImage)
//                .size(128, 128)
//                .outputFormat("jpg")
//                .asBufferedImage();
//
//        // 그레이스케일 변환
//        BufferedImage grayImage = new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_GRAY);
//        grayImage.getGraphics().drawImage(resizedImage, 0, 0, null);
//
//        // 이미지를 바이트 배열로 변환
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(grayImage, "jpg", baos);
//        return baos.toByteArray();
//    }
//
//    private String changeFileName(String originalFileName) {
//        // 업로드할 파일의 이름을 변경하는 로직
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        return originalFileName + "_" + LocalDateTime.now().format(formatter) + ".jpg";
//    }
//}