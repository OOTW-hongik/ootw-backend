package OOTWhongik.OOTW.domain.outfit.controller;

import OOTWhongik.OOTW.domain.outfit.dto.response.WeatherSummary;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitDetailResponse;
import OOTWhongik.OOTW.domain.outfit.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.domain.outfit.dto.request.OutfitRequest;
import OOTWhongik.OOTW.domain.outfit.service.OutfitService;
import OOTWhongik.OOTW.domain.outfit.service.WeatherUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
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
    private final WeatherUtil weatherUtil;

    @Operation(summary = "outfit list", description = "착장 리스트 조회")
    @GetMapping("/list")
    public OutfitListResponse getOutfitList (@RequestParam(required = false) Optional<Integer> quantity) throws IOException {
        return outfitService.getOutfitList(quantity);
    }

    @Operation(summary = "outfit detail", description = "착장 상세 페이지")
    @GetMapping("/{outfitId}")
    public OutfitDetailResponse outfitDetail(@PathVariable Long outfitId) throws Exception {
        return outfitService.getOutfitDetail(outfitId);
    }

    @Operation(summary = "outfit register", description = "착장 생성 시 날씨 조회")
    @GetMapping("/register")
    public WeatherSummary outfitRegister(@RequestParam String outfitDate,
                                         @RequestParam String outfitLocation) throws IOException {
        return weatherUtil.getWeatherSummary(outfitDate, outfitLocation);
    }

    @Operation(summary = "outfit creation", description = "착장 생성")
    @PostMapping
    public ResponseEntity<?> saveOutfit(@RequestBody OutfitRequest outfitRequest) throws IOException {
        outfitService.saveOutfit(outfitRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "outfit update", description = "착장 수정")
    @PutMapping("/{outfitId}")
    public ResponseEntity<?> updateOutfit(@PathVariable Long outfitId,
                                          @RequestBody OutfitRequest outfitRequest) throws Exception {
        outfitService.updateOutfit(outfitId, outfitRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "outfit delete", description = "착장 삭제")
    @DeleteMapping("/{outfitId}")
    public ResponseEntity<?> outfitDelete(@PathVariable Long outfitId) throws Exception {
        outfitService.deleteOutfit(outfitId);
        return ResponseEntity.ok().build();
    }

}
