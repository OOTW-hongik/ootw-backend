package OOTWhongik.OOTW.controller;

import OOTWhongik.OOTW.dto.HomeResponse;
import OOTWhongik.OOTW.service.MemberService;
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
    private final MemberService memberService;

    @Operation(summary = "home", description = "home")
    @GetMapping("/home")
    public HomeResponse home(@RequestParam Long memberId) {
        return memberService.getHome(memberId);
    }
}
