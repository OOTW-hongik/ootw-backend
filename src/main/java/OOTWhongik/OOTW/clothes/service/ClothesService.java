package OOTWhongik.OOTW.clothes.service;

import OOTWhongik.OOTW.clothes.dto.request.ClothesRequest;
import OOTWhongik.OOTW.clothes.exception.ClothesNotFoundException;
import OOTWhongik.OOTW.clothes.domain.Clothes;
import OOTWhongik.OOTW.clothes.domain.Photo;
import OOTWhongik.OOTW.clothes.dto.response.ClothesResponse;
import OOTWhongik.OOTW.clothes.exception.ClothesInUseException;
import OOTWhongik.OOTW.clothes.exception.UnauthorizedClothesAccessException;
import OOTWhongik.OOTW.clothes.repository.ClothesRepository;
import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.member.repository.MemberRepository;
import OOTWhongik.OOTW.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClothesService {
    private final ClothesRepository clothesRepository;
    private final PhotoService photoService;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveClothes(ClothesRequest clothesRequest, MultipartFile file) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Clothes clothes = clothesRequest.toEntity(); // 사진 아직 저장되지 않음
        clothes.addMember(memberRepository.findById(memberId).get());
        clothesRepository.save(clothes);
        Photo photo = photoService.uploadPhoto(file, clothes.getId());
        clothes.setPhoto(photo);
        return clothes.getId();
    }

    @Transactional
    public ClothesResponse updateClothes(Long clothesId, ClothesRequest clothesRequest, MultipartFile file) throws Exception {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesNotFoundException("id가 " + clothesId + "인 옷을 찾지 못했습니다."));
        if (!member.contains(clothes)) {
            throw new UnauthorizedClothesAccessException("해당 유저는 id가" + clothesId + "인 옷을 소유하고 있지 않습니다.");
        }
        clothes.update(clothesRequest);
        Photo photo = photoService.updatePhoto(file, clothes.getId(), clothes.getPhoto().getId());
        clothes.setPhoto(photo);
        return ClothesResponse.from(clothes);
    }


    @Transactional
    public ClothesResponse updateClothes(Long clothesId, ClothesRequest clothesRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesNotFoundException("id가 " + clothesId + "인 옷을 찾지 못했습니다."));
        if (!member.contains(clothes)) {
            throw new UnauthorizedClothesAccessException("해당 유저는 id가" + clothesId + "인 옷을 소유하고 있지 않습니다.");
        }
        clothes.update(clothesRequest);
        return ClothesResponse.from(clothes);
    }

    @Transactional
    public void deleteClothes(Long clothesId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesNotFoundException("id가 " + clothesId + "인 옷을 찾지 못했습니다."));
        if (!member.contains(clothes)) {
            throw new UnauthorizedClothesAccessException("해당 유저는 id가" + clothesId + "인 옷을 소유하고 있지 않습니다.");
        }
        if (!clothes.getClothesOutfitList().isEmpty())
            throw new ClothesInUseException("옷이 쓰이고 있는 착장이 있어 삭제할 수 없습니다.");
        photoService.deletePhoto(clothes.getPhoto());
        clothesRepository.delete(clothes);
    }
}
