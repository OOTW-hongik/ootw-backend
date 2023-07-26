package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.domain.Photo;
import OOTWhongik.OOTW.dto.request.ClothesRequest;
import OOTWhongik.OOTW.dto.request.ClothesUpdateRequest;
import OOTWhongik.OOTW.repository.ClothesRepository;
import OOTWhongik.OOTW.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClothesService {
    private final ClothesRepository clothesRepository;
    private final PhotoService photoService;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveClothes(ClothesRequest clothesRequest, MultipartFile file) throws IOException {
        Clothes clothes = clothesRequest.toEntity(memberRepository); // 사진 아직 저장되지 않음
        clothesRepository.save(clothes);
        Photo photo = photoService.uploadPhoto(file, clothes.getId());
        clothes.setPhoto(photo);
        clothesRepository.save(clothes);
    }

    @Transactional
    public void updateClothes(ClothesUpdateRequest clothesUpdateRequest, MultipartFile file) throws IOException {
        Clothes newClothes = clothesUpdateRequest.toEntity();
        Clothes oldClothes = clothesRepository.findById(clothesUpdateRequest.getClothesId()).get();
        Photo photo = photoService.updatePhoto(file, newClothes.getId(), oldClothes.getPhoto().getId());
        newClothes.setPhoto(photo);
        clothesRepository.save(newClothes);
    }
}
