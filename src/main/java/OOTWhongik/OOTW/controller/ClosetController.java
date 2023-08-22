package OOTWhongik.OOTW.controller;

import OOTWhongik.OOTW.dto.request.ClothesUpdateRequest;
import OOTWhongik.OOTW.dto.response.ClosetResponse;
import OOTWhongik.OOTW.dto.request.ClothesRequest;
import OOTWhongik.OOTW.dto.response.ClothesDetailResponse;
import OOTWhongik.OOTW.service.ClosetService;
import OOTWhongik.OOTW.service.ClothesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "closet", description = "closet")
@RequiredArgsConstructor
@RestController
public class ClosetController {

    private final ClosetService closetService;
    private final ClothesService clothesService;

    @Operation(summary = "closet", description = "옷장 페이지")
    @GetMapping("/closet")
    public ClosetResponse getCloset(@RequestParam Long memberId, @RequestParam String category) {
        return closetService.getCloset(memberId, category);
    }

    @Operation(summary = "hidden closet", description = "숨긴 옷장 페이지")
    @GetMapping("/closet/hidden")
    public ClosetResponse getHiddenCloset(@RequestParam Long memberId, @RequestParam String category) {
        return closetService.getHiddenCloset(memberId, category);
    }

    @Operation(summary = "clothes creation", description = "옷 등록")
    @PostMapping("/clothes")
    public ResponseEntity<?> saveClothes(@RequestPart ClothesRequest clothesRequest,
                                         @RequestPart MultipartFile clothesPhoto) throws IOException {
        clothesService.saveClothes(clothesRequest, clothesPhoto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "clothes update", description = "옷 수정")
    @PutMapping("/clothes")
    public ResponseEntity<?> updateClothes(@RequestPart ClothesUpdateRequest clothesUpdateRequest,
                                           @RequestPart MultipartFile clothesPhoto) throws IOException {
        clothesService.updateClothes(clothesUpdateRequest, clothesPhoto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "clothes update", description = "사진제외 옷 수정")
    @PatchMapping("/clothes")
    public ResponseEntity<?> updateClothesWithoutPhoto(@RequestBody ClothesUpdateRequest clothesUpdateRequest) {
        clothesService.updateClothes(clothesUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "clothes detail", description = "옷 상세 페이지")
    @GetMapping("/clothes")
    public ClothesDetailResponse getClothes(@RequestParam Long clothesId) {
        return closetService.getClothes(clothesId);
    }

    @Operation(summary = "clothes delete", description = "옷 삭제")
    @DeleteMapping("/clothes")
    public ResponseEntity<?> deleteClothes(@RequestParam Long clothesId) throws Exception {
        clothesService.deleteClothes(clothesId);
        return ResponseEntity.ok().build();
    }

}
