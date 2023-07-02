package OOTWhongik.OOTW.controller;

import OOTWhongik.OOTW.dto.response.OutfitListResponse;
import OOTWhongik.OOTW.dto.request.OutfitRequest;
import OOTWhongik.OOTW.service.OutfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "outfit", description = "outfit")
@RequiredArgsConstructor
@RestController
@RequestMapping("/outfit")
public class OutfitController {
    private final OutfitService outfitService;

    @Operation(summary = "outfit creation", description = "착장 생성")
    @PostMapping("/")
    public ResponseEntity<?> saveOutfit(@RequestBody OutfitRequest outfitRequest) {
        outfitService.saveOutfit(outfitRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "outfit list", description = "착장 리스트 조회")
    @GetMapping("/list")
    public OutfitListResponse getOutfitList (@RequestParam Long memberId) {
        return outfitService.getOutfitList(memberId);
    }
}
