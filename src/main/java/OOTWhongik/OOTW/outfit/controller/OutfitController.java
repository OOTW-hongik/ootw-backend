package OOTWhongik.OOTW.outfit.controller;

import OOTWhongik.OOTW.member.domain.Location;
import OOTWhongik.OOTW.outfit.dto.request.OutfitRequest;
import OOTWhongik.OOTW.outfit.dto.response.OutfitDetailResponse;
import OOTWhongik.OOTW.outfit.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.outfit.dto.response.WeatherSummary;
import OOTWhongik.OOTW.outfit.service.OutfitService;
import OOTWhongik.OOTW.outfit.service.WeatherUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@Tag(name = "outfit", description = "outfit")
@RequiredArgsConstructor
@RestController
@RequestMapping("/outfit")
public class OutfitController {
    private final OutfitService outfitService;
    private final WeatherUtil weatherUtil;

    @Operation(summary = "outfit list", description = "착장 리스트 조회")
    @GetMapping("/list")
    public OutfitListResponse getOutfitList (@RequestParam(required = false) Optional<Integer> quantity) {
        return outfitService.getOutfitList(quantity);
    }

    @Operation(summary = "outfit detail", description = "착장 상세 페이지")
    @GetMapping("/{outfitId}")
    public OutfitDetailResponse outfitDetail(@PathVariable Long outfitId) {
        return outfitService.getOutfitDetail(outfitId);
    }

    @Operation(summary = "outfit register", description = "착장 생성 시 날씨 조회")
    @GetMapping("/register")
    public WeatherSummary outfitRegister(@RequestParam String outfitDate,
                                         @RequestParam Location outfitLocation) {
        return weatherUtil.getWeatherSummary(outfitDate, outfitLocation);
    }

    @Operation(summary = "outfit creation", description = "착장 생성")
    @PostMapping
    public ResponseEntity<?> saveOutfit(@RequestBody @Valid OutfitRequest outfitRequest) {
        Long outfitId = outfitService.saveOutfit(outfitRequest);
        return ResponseEntity
                .created(URI.create("/outfit/" + outfitId))
                .build();
    }

    @Operation(summary = "outfit update", description = "착장 수정")
    @PutMapping("/{outfitId}")
    public ResponseEntity<?> updateOutfit(@PathVariable Long outfitId,
                                          @RequestBody @Valid OutfitRequest outfitRequest) {
        outfitService.updateOutfit(outfitId, outfitRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "outfit delete", description = "착장 삭제")
    @DeleteMapping("/{outfitId}")
    public ResponseEntity<?> outfitDelete(@PathVariable Long outfitId) {
        outfitService.deleteOutfit(outfitId);
        return ResponseEntity.noContent().build();
    }

}
