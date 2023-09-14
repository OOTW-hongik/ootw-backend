package OOTWhongik.OOTW.domain.outfit.controller;

import OOTWhongik.OOTW.domain.outfit.dto.response.WeatherSummary;
import OOTWhongik.OOTW.domain.outfit.dto.request.OutfitUpdateRequest;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitDetailResponse;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.domain.outfit.dto.request.OutfitRequest;
import OOTWhongik.OOTW.domain.outfit.service.OutfitService;
import OOTWhongik.OOTW.domain.outfit.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "outfit", description = "outfit")
@RequiredArgsConstructor
@RestController
@RequestMapping("/outfit")
public class OutfitController {
    private final OutfitService outfitService;
    private final WeatherService weatherService;

    @Operation(summary = "outfit creation", description = "착장 생성")
    @PostMapping("/")
    public ResponseEntity<?> saveOutfit(@RequestBody OutfitRequest outfitRequest) {
        outfitService.saveOutfit(outfitRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "outfit update", description = "착장 수정")
    @PutMapping("/")
    public ResponseEntity<?> updateOutfit(@RequestBody OutfitUpdateRequest outfitUpdateRequest) {
        outfitService.updateOutfit(outfitUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "outfit list", description = "착장 리스트 조회")
    @GetMapping("/list")
    public OutfitListResponse getOutfitList (@RequestParam Long memberId) {
        return outfitService.getOutfitList(memberId);
    }

    @Operation(summary = "outfit register page", description = "착장 생성 시 날씨 조회")
    @GetMapping("/register")
    public WeatherSummary outfitRegister(@RequestParam String outfitDate, @RequestParam String outfitLocation) throws IOException {
        return weatherService.getWeatherInfo(outfitDate, outfitLocation);
    }

    @Operation(summary = "outfit detail", description = "착장 상세 페이지")
    @GetMapping("/")
    public OutfitDetailResponse outfitDetail(@RequestParam Long outfitId) {
        return outfitService.getOutfitDetail(outfitId);
    }

    @Operation(summary = "outfit delete", description = "착장 삭제")
    @DeleteMapping("/")
    public ResponseEntity<?> outfitDelete(@RequestParam Long outfitId) {
        outfitService.deleteOutfit(outfitId);
        return ResponseEntity.ok().build();
    }

}