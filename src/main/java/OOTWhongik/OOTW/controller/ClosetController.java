package OOTWhongik.OOTW.controller;

import OOTWhongik.OOTW.dto.ClosetResponse;
import OOTWhongik.OOTW.dto.ClothesRequest;
import OOTWhongik.OOTW.service.ClosetService;
import OOTWhongik.OOTW.service.ClothesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "closet", description = "closet")
@RequiredArgsConstructor
@RestController
public class ClosetController {

    private final ClosetService closetService;
    private final ClothesService clothesService;

    @Operation(summary = "closet", description = "옷장 페이지")
    @GetMapping("/closet")
    public ClosetResponse getCloset(@RequestParam Long memberId, @RequestParam String category) throws JsonProcessingException {
        return closetService.getCloset(memberId, category);
    }

    @Operation(summary = "clothes creation", description = "옷 등록")
    @PostMapping("/clothes")
    public ResponseEntity<?> saveClothes(@RequestBody ClothesRequest clothesRequest) {
        clothesService.saveClothes(clothesRequest);
        return ResponseEntity.ok().build();
    }
}
