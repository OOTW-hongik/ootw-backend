package OOTWhongik.OOTW.domain.member.controller;

import OOTWhongik.OOTW.domain.member.dto.request.LocationUpdateRequest;
import OOTWhongik.OOTW.domain.member.dto.response.HomeResponse;
import OOTWhongik.OOTW.domain.member.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "home", description = "home")
@RequestMapping("/home")
@RequiredArgsConstructor
@RestController
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "home", description = "home")
    @GetMapping
    public HomeResponse home(@RequestParam Long memberId) throws IOException {
        return homeService.getHome(memberId);
    }

    @Operation(summary = "update location", description = "update location")
    @PutMapping("/location")
    public ResponseEntity<?> updateLocation(@RequestBody LocationUpdateRequest locationUpdateRequest) {
        homeService.updateLocation(locationUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health_check")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
