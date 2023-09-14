package OOTWhongik.OOTW.domain.clothes.service;

import OOTWhongik.OOTW.domain.clothes.domain.Photo;
import OOTWhongik.OOTW.domain.clothes.repository.PhotoRepository;
import OOTWhongik.OOTW.global.common.filecontrol.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {

    private final S3FileComponent s3FileComponent;
    private final PhotoRepository photoRepository;

    public Photo uploadPhoto(MultipartFile file, Long clothesId) throws IOException {
        List<String> uploadImageUrl = s3FileComponent.upload(file, "ootw", Long.toString(clothesId));
        Photo photo = Photo.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFilePath(uploadImageUrl.get(0))
                .fileSize(file.getSize())
                .build();
        photoRepository.save(photo);
        return photo;
    }

    public Photo updatePhoto (MultipartFile file, Long clothesId, Long photoId) throws IOException {
        Photo photo = photoRepository.findById(photoId).get();
        List<String> uploadImageUrl = s3FileComponent.upload(file, "ootw", Long.toString(clothesId));
        photo.update(file.getOriginalFilename(), uploadImageUrl.get(0) ,file.getSize());
        photoRepository.save(photo);
        return photo;
    }

    public void deletePhoto(Photo photo) {
        s3FileComponent.delete("ootw", Long.toString(photo.getId()));
        photoRepository.delete(photo);
    }
}
