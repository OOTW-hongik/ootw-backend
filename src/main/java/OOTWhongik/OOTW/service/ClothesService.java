package OOTWhongik.OOTW.service;

import OOTWhongik.OOTW.dto.ClothesRequest;
import OOTWhongik.OOTW.repository.ClothesRepository;
import OOTWhongik.OOTW.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothesService {
    private final ClothesRepository clothesRepository;
    private final MemberRepository memberRepository;

    public void saveClothes(ClothesRequest clothesRequest) {
        clothesRepository.save(clothesRequest.toEntity(memberRepository));
    }

}
