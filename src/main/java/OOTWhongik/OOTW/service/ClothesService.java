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
        Clothes clothes = clothesRepository.findById(clothesUpdateRequest.getClothesId()).get();
        clothes.update(clothesUpdateRequest);
        Photo photo = photoService.updatePhoto(file, clothes.getId(), clothes.getPhoto().getId());
        clothes.setPhoto(photo);
    }

    @Transactional
    public void updateClothes(ClothesUpdateRequest clothesUpdateRequest) {
        Clothes clothes = clothesRepository.findById(clothesUpdateRequest.getClothesId()).get();
        clothes.update(clothesUpdateRequest);
    }

    @Transactional
    public void deleteClothes(Long clothesId) throws Exception {
        Clothes clothes = clothesRepository.findById(clothesId).get();
        if (clothes.getClothesOutfitList().size() > 0)
            throw new Exception("옷이 쓰인 착장이 없어야 삭제가 가능합니다.");
        photoService.deletePhoto(clothes.getPhoto());
        clothesRepository.delete(clothes);
    }
}
