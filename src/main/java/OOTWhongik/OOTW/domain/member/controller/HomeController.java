package OOTWhongik.OOTW.domain.member.controller;

import OOTWhongik.OOTW.domain.member.dto.request.LocationRequest;
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
    public HomeResponse home() throws IOException {
        return homeService.getHome();
    }

    @Operation(summary = "update location", description = "update location")
    @PutMapping("/location")
    public ResponseEntity<?> updateLocation(@RequestBody LocationRequest locationRequest) {
        homeService.updateLocation(locationRequest);
        return ResponseEntity.ok().build();
    }

}
