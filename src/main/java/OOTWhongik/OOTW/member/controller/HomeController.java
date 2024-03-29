package OOTWhongik.OOTW.member.controller;

import OOTWhongik.OOTW.member.dto.request.LocationRequest;
import OOTWhongik.OOTW.member.dto.response.HomeResponse;
import OOTWhongik.OOTW.member.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "home", description = "home")
@RequestMapping("/home")
@RequiredArgsConstructor
@RestController
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "home", description = "home")
    @GetMapping
    public HomeResponse home() {
        return homeService.getHome();
    }

    @Operation(summary = "update location", description = "update location")
    @PutMapping("/location")
    public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationRequest locationRequest) {
        homeService.updateLocation(locationRequest);
        return ResponseEntity.ok().build();
    }

}
