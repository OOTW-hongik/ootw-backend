package OOTWhongik.OOTW.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "swagger", description = "swagger test")
@RestController
public class HomeController {

    @Operation(summary = "home", description = "home test")
    @ApiResponse(responseCode = "200", description="ok")
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
