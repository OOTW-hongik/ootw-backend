package OOTWhongik.OOTW.controller;

import OOTWhongik.OOTW.dto.HomeResponse;
import OOTWhongik.OOTW.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "home", description = "home")
@RequiredArgsConstructor
@RestController
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "home", description = "home")
    @GetMapping("/home")
    public HomeResponse home(@RequestParam Long memberId) {
        return homeService.getHome(memberId);
    }
}
