package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Photo;
import OOTWhongik.OOTW.filecontrol.S3FileComponent;
import OOTWhongik.OOTW.repository.PhotoRepository;
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
        Photo photo = null;
        if (file.isEmpty()) {
            // TODO : 파일이 없을 땐 어떻게 해야할까.. 고민을 해보아야 할 것 예외처리 해야함
        }
        else{
            String dirName = "ootw";
            List<String> uploadImageUrl = s3FileComponent.upload(file, dirName, Long.toString(clothesId));
            photo = Photo.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFilePath(uploadImageUrl.get(0))
                    .fileSize(file.getSize())
                    .build();
            photoRepository.save(photo);
        }
        return photo;
    }

    public Photo updatePhoto (MultipartFile file, Long clothesId, Long photoId) throws IOException {
        Photo photo = null;
        if (file.isEmpty()) {
            // TODO : 파일이 없을 땐 어떻게 해야할까.. 고민을 해보아야 할 것 예외처리 해야함
        }
        else{
            String dirName = "ootw";
            List<String> uploadImageUrl = s3FileComponent.upload(file, dirName, Long.toString(clothesId));
            photo = Photo.builder()
                    .id(photoId)
                    .originalFileName(file.getOriginalFilename())
                    .storedFilePath(uploadImageUrl.get(0))
                    .fileSize(file.getSize())
                    .build();
            photoRepository.save(photo);
        }
        return photo;
    }
}
