package OOTWhongik.OOTW.clothes.service;

import OOTWhongik.OOTW.clothes.dto.response.ClosetResponse;
import OOTWhongik.OOTW.clothes.exception.ClothesNotFoundException;
import OOTWhongik.OOTW.clothes.domain.Category;
import OOTWhongik.OOTW.clothes.domain.Clothes;
import OOTWhongik.OOTW.clothes.exception.UnauthorizedClothesAccessException;
import OOTWhongik.OOTW.member.domain.Member;
import OOTWhongik.OOTW.clothes.dto.response.ClothesDetailResponse;
import OOTWhongik.OOTW.clothes.dto.response.ClothesResponse;
import OOTWhongik.OOTW.clothes.repository.ClothesRepository;
import OOTWhongik.OOTW.member.repository.MemberRepository;
import OOTWhongik.OOTW.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClosetService {

    private final ClothesRepository clothesRepository;
    private final MemberRepository memberRepository;

    public ClosetResponse getCloset(String categoryName) {
        return getCloset(categoryName, false);
    }

    public ClosetResponse getHiddenCloset(String categoryName) {
        return getCloset(categoryName, true);
    }

    public ClosetResponse getCloset(String categoryName, boolean hidden) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.getReferenceById(memberId);
        List<Clothes> clothesList =
                clothesRepository.findAllByMemberAndCategory(member, Category.findByName(categoryName));

        Set<String> subCategoryCollection = new TreeSet<>();
        List<ClothesResponse> clothesResponses = new ArrayList<>();
        clothesList.stream()
                .filter(clothes -> clothes.isHidden() == hidden)
                .forEachOrdered(clothes -> {
                    subCategoryCollection.add(clothes.getSubcategory());
                    clothesResponses.add(ClothesResponse.from(clothes));
                });

        return ClosetResponse.builder()
                .subCategoryName(new ArrayList<>(subCategoryCollection))
                .clothesList(clothesResponses)
                .build();
    }

    public ClothesDetailResponse getClothes(Long clothesId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.getReferenceById(memberId);
        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesNotFoundException("id가 " + clothesId + "인 옷을 찾지 못했습니다."));
        if (!member.contains(clothes)) {
            throw new UnauthorizedClothesAccessException("해당 유저는 id가" + clothesId + "인 옷을 소유하고 있지 않습니다.");
        }
        return ClothesDetailResponse.builder()
                .clothesId(clothesId)
                .clothesUrl(clothes.getPhoto().getStoredFilePath())
                .subCategory(clothes.getSubcategory())
                .clothesComment(clothes.getClothesComment())
                .hidden(clothes.isHidden())
                .build();
    }
}
