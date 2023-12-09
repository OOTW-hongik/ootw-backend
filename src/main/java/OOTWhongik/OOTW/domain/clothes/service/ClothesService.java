package OOTWhongik.OOTW.domain.clothes.service;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.clothes.domain.Photo;
import OOTWhongik.OOTW.domain.clothes.dto.request.ClothesRequest;
import OOTWhongik.OOTW.domain.clothes.repository.ClothesRepository;
import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.member.repository.MemberRepository;
import OOTWhongik.OOTW.global.config.security.SecurityUtil;
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
        Clothes clothes = clothesRepository.findById(clothesId).get();
        if (!isOwner(member, clothes)) {
            throw new Exception("옷의 소유주가 아닙니다.");
        }
        clothes.update(clothesRequest);
        Photo photo = photoService.updatePhoto(file, clothes.getId(), clothes.getPhoto().getId());
        clothes.setPhoto(photo);
        return ClothesResponse.from(clothes);
    }


    @Transactional
    public ClothesResponse updateClothes(Long clothesId, ClothesRequest clothesRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Clothes clothes = clothesRepository.findById(clothesId).get();
        if (!isOwner(member, clothes)) {
            throw new Exception("옷의 소유주가 아닙니다.");
        }
        clothes.update(clothesRequest);
        return ClothesResponse.from(clothes);
    }

    @Transactional
    public void deleteClothes(Long clothesId) throws Exception {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
        Clothes clothes = clothesRepository.findById(clothesId).get();
        if (!isOwner(member, clothes)) {
            throw new Exception("옷의 소유주가 아닙니다.");
        }
        if (clothes.getClothesOutfitList().size() > 0)
            throw new Exception("옷이 쓰인 착장이 없어야 삭제가 가능합니다.");
        photoService.deletePhoto(clothes.getPhoto());
        clothesRepository.delete(clothes);
    }

    private boolean isOwner(Member member, Clothes clothes) {
        return clothes.getMember() == member;
    }
}
