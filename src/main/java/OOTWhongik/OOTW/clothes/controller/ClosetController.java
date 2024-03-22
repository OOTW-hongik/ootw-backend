package OOTWhongik.OOTW.clothes.controller;

import OOTWhongik.OOTW.clothes.dto.request.ClothesRequest;
import OOTWhongik.OOTW.clothes.dto.response.ClosetResponse;
import OOTWhongik.OOTW.clothes.dto.response.ClothesDetailResponse;
import OOTWhongik.OOTW.clothes.dto.response.ClothesResponse;
import OOTWhongik.OOTW.clothes.service.ClosetService;
import OOTWhongik.OOTW.clothes.service.ClothesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@Tag(name = "closet", description = "closet")
@RequiredArgsConstructor
@RestController
public class ClosetController {

    private final ClosetService closetService;
    private final ClothesService clothesService;

    @Operation(summary = "closet", description = "옷장 페이지")
    @GetMapping("/closet")
    public ClosetResponse getCloset(@RequestParam String category) {
        return closetService.getCloset(category);
    }

    @Operation(summary = "hidden closet", description = "숨긴 옷장 페이지")
    @GetMapping("/closet/hidden")
    public ClosetResponse getHiddenCloset(@RequestParam String category) {
        return closetService.getHiddenCloset(category);
    }

    @Operation(summary = "clothes creation", description = "옷 등록")
    @PostMapping("/clothes")
    public ResponseEntity<?> saveClothes(@RequestPart @Valid ClothesRequest clothesRequest,
                                         @RequestPart MultipartFile clothesPhoto) {
        long clothesId = clothesService.saveClothes(clothesRequest, clothesPhoto);
        return ResponseEntity
                .created(URI.create("/clothes/" + clothesId))
                .build();
    }

    @Operation(summary = "clothes update", description = "옷 수정")
    @PutMapping("/clothes/{clothesId}")
    public ResponseEntity<?> updateClothes(@PathVariable Long clothesId,
                                           @RequestPart @Valid ClothesRequest clothesRequest,
                                           @RequestPart MultipartFile clothesPhoto) throws Exception {
        ClothesResponse clothesResponse = clothesService.updateClothes(clothesId, clothesRequest, clothesPhoto);
        return ResponseEntity.ok().body(clothesResponse);
    }

    @Operation(summary = "clothes update", description = "사진제외 옷 수정")
    @PatchMapping("/clothes/{clothesId}")
    public ResponseEntity<?> updateClothesWithoutPhoto(@PathVariable Long clothesId,
                                                       @RequestBody @Valid ClothesRequest clothesRequest) {
        ClothesResponse clothesResponse = clothesService.updateClothes(clothesId, clothesRequest);
        return ResponseEntity.ok().body(clothesResponse);
    }

    @Operation(summary = "clothes detail", description = "옷 상세 페이지")
    @GetMapping("/clothes/{clothesId}")
    public ClothesDetailResponse getClothes(@PathVariable Long clothesId) {
        return closetService.getClothes(clothesId);
    }

    @Operation(summary = "clothes delete", description = "옷 삭제")
    @DeleteMapping("/clothes/{clothesId}")
    public ResponseEntity<?> deleteClothes(@PathVariable Long clothesId) {
        clothesService.deleteClothes(clothesId);
        return ResponseEntity.noContent().build();
    }

}
