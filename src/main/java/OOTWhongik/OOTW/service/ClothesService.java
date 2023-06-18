package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.dto.ClothesRequest;
import OOTWhongik.OOTW.repository.ClothesRepository;
import OOTWhongik.OOTW.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClothesService {
    private final ClothesRepository clothesRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveClothes(ClothesRequest clothesRequest) {
        clothesRepository.save(clothesRequest.toEntity(memberRepository));
    }

}
