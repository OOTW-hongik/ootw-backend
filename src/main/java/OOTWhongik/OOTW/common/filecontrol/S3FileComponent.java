package OOTWhongik.OOTW.common.filecontrol;

import OOTWhongik.OOTW.clothes.exception.FileDeleteException;
import OOTWhongik.OOTW.clothes.exception.FileUploadException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3FileComponent {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public List<String> upload(MultipartFile multipartFile, String dirName, String fileName) throws IOException {
        return upload(new MultipartFile[] {multipartFile}, dirName, fileName);
    }

    public List<String> upload(MultipartFile[] multipartFile, String dirName, String fileName) throws IOException {
        for (MultipartFile mf : multipartFile) {
            String contentType = mf.getContentType();
            if (ObjectUtils.isEmpty(contentType)) {
                throw new FileUploadException("파일의 contentType이 없습니다.");
            } else if (!(contentType.equals(ContentType.IMAGE_JPEG.toString())
                    || contentType.equals(ContentType.IMAGE_PNG.toString()))){
                throw new FileUploadException("파일의 contentType이 지원되지 않습니다..");
            }
        }
        List<String> listUrl = new ArrayList<>();
        for (MultipartFile mf : multipartFile) {
            File uploadFile = convert(mf)
                    .orElseThrow(() -> new FileUploadException("MultipartFile -> File로 전환이 실패했습니다."));
            String uploadImageUrl = putS3(uploadFile, dirName + "/" + fileName);
            removeNewFile(uploadFile);
            listUrl.add(uploadImageUrl);
        }
        return listUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (!targetFile.delete()) {
            throw new FileUploadException("임시 저장 파일을 삭제하는데 실패했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    public void delete(String dirName, String fileName) { // 객체 삭제 filePath : 폴더명/파일네임.파일확장자
        String filePath = dirName + "/" + fileName;
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filePath));
        } catch (AmazonServiceException e) {
            throw new FileDeleteException("파일 삭제에 실패했습니다.");
        }
    }

    public ResponseEntity<byte[]> download(String fileUrl) throws IOException {
        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, fileUrl));
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(contentType(fileUrl));
        httpHeaders.setContentLength(bytes.length);
        String[] arr = fileUrl.split("/");
        String type = arr[arr.length-1];
        String fileName = URLEncoder.encode(type, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        httpHeaders.setContentDispositionFormData("attachment", fileName); // 다운로드 되는 파일 이름을 바꾸고 싶다면 여기서 조정

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    private MediaType contentType(String keyName) {
        String[] arr = keyName.split("\\.");
        String type = arr[arr.length-1];
        return switch (type) {
            case "txt" -> MediaType.TEXT_PLAIN;
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    public List<String> getUrls (String dirName, List<String> fileNames) {
        List<String> urls = new ArrayList<>();
        for(String fileName : fileNames) {
            if (amazonS3Client.doesObjectExist(bucket, dirName + "/" + fileName)) {
                urls.add((amazonS3Client.getUrl(bucket, dirName + "/" + fileName)).toString());
            }
        }
        return urls;
    }
}
