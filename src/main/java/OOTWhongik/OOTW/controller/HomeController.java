package OOTWhongik.OOTW.controller;

import OOTWhongik.OOTW.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "swagger", description = "swagger test")
@RestController
public class HomeController {

    @Operation(summary = "home", description = "home test")
    @GetMapping("/home")
    public Member home() {
        return Member.builder()
                .name("name")
                .email("email")
                .location("서울")
                .build();
    }

}
