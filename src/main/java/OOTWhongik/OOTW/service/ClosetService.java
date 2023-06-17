package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.dto.ClosetResponse;
import OOTWhongik.OOTW.dto.ClothesList;
import OOTWhongik.OOTW.repository.ClothesRepository;
import OOTWhongik.OOTW.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClosetService {

    private final ClothesRepository clothesRepository;
    private final MemberRepository memberRepository;

    public ClosetResponse getCloset(Long memberId, String category) throws JsonProcessingException {
        Member member = memberRepository.findById(memberId).get();
        List<Clothes> clothesList;
        if (clothesRepository.findAllByMemberAndCategory(member, category).isPresent()) {
            clothesList = clothesRepository.findAllByMemberAndCategory(member, category).get();
        } else {
            throw new RuntimeException("등록된 옷이 없습니다.");
        }
        List<String> subCategoryCollection = new ArrayList<>();
        List<ClothesList> clothesListParam = new ArrayList<>();
        for (Clothes clothes : clothesList) {
            //subcategory 가 있는지 검사하고 없으면 삽입
            if (!subCategoryCollection.contains(clothes.getSubcategory())) {
                subCategoryCollection.add(clothes.getSubcategory());
            }

            //clothes 를 삽입
            clothesListParam.add(ClothesList.builder()
                            .clothesId(clothes.getId())
                            .clothesUrl(clothes.getPhotoUrl())
                            .build());
        }

        return new ClosetResponse(subCategoryCollection, clothesListParam);
    }
}