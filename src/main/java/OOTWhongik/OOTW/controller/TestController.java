package OOTWhongik.OOTW.controller;

import OOTWhongik.OOTW.domain.Clothes;
import OOTWhongik.OOTW.domain.Member;
import OOTWhongik.OOTW.repository.ClothesRepository;
import OOTWhongik.OOTW.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "test", description = "test")
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {
    private final MemberRepository memberRepository;
    private final ClothesRepository clothesRepository;

    @Operation(summary = "member test", description = "member test")
    @GetMapping("/member")
    public List<Member> memberTest() {
        return memberRepository.findAll();
    }

    @Operation(summary = "clothes test", description = "clothes test")
    @GetMapping("/clothes")
    public List<Clothes> clothesTest() {
        return clothesRepository.findAll();
    }

//    @Operation(summary = "member remove test", description = "member remove test")
//    @DeleteMapping("/member")
//    public ResponseEntity<?> memberRemoveTest(@RequestParam Long memberId) {
//        memberRepository.deleteById(memberId);
//        return ResponseEntity.ok().build();
//    }

    @Operation(summary = "clothes remove test", description = "clothes remove test")
    @DeleteMapping("/clothes")
    public ResponseEntity<?> clothesRemoveTest(@RequestParam Long clothesId) {
        clothesRepository.deleteById(clothesId);
        return ResponseEntity.ok().build();
    }
}
