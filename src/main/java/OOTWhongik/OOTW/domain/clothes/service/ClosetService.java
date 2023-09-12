package OOTWhongik.OOTW.domain.clothes.service;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.member.domain.Member;
import OOTWhongik.OOTW.domain.clothes.dto.response.ClosetResponse;
import OOTWhongik.OOTW.domain.clothes.dto.response.ClothesDetailResponse;
import OOTWhongik.OOTW.domain.clothes.dto.response.ClothesResponse;
import OOTWhongik.OOTW.domain.clothes.repository.ClothesRepository;
import OOTWhongik.OOTW.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@Transactional
@RequiredArgsConstructor
public class ClosetService {

    private final ClothesRepository clothesRepository;
    private final MemberRepository memberRepository;

    public ClosetResponse getCloset(Long memberId, String category) {
        return getCloset(memberId, category, false);
    }

    public ClosetResponse getHiddenCloset(Long memberId, String category) {
        return getCloset(memberId, category, true);
    }

    public ClosetResponse getCloset(Long memberId, String category, boolean hidden) {
        Member member = memberRepository.findById(memberId).get();
        List<Clothes> clothesList;
        if (clothesRepository.findAllByMemberAndCategory(member, category).isPresent()) {
            clothesList = clothesRepository.findAllByMemberAndCategory(member, category).get();
        } else {
            throw new RuntimeException("등록된 옷이 없습니다.");
        }
        Set<String> subCategoryCollection = new TreeSet<>();
        List<ClothesResponse> clothesResponseParam = new ArrayList<>();
        for (Clothes clothes : clothesList) {
            if (clothes.isHidden() != hidden) continue;

            subCategoryCollection.add(clothes.getSubcategory());
            //clothes 를 삽입
            clothesResponseParam.add(ClothesResponse.builder()
                            .clothesId(clothes.getId())
                            .clothesUrl(clothes.getPhoto().getStoredFilePath())
                            .subCategory(clothes.getSubcategory())
                            .build());
        }

        return new ClosetResponse(new ArrayList<>(subCategoryCollection), clothesResponseParam);
    }

    public ClothesDetailResponse getClothes(Long clothesId) {
        Clothes clothes = clothesRepository.findById(clothesId).get();
        return ClothesDetailResponse.builder()
                .clothesId(clothesId)
                .clothesUrl(clothes.getPhoto().getStoredFilePath())
                .subCategory(clothes.getSubcategory())
                .clothesComment(clothes.getClothesComment())
                .hidden(clothes.isHidden())
                .build();
    }
}
