package OOTWhongik.OOTW.global.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Global", description = "Global")
@RequiredArgsConstructor
@RestController
public class GlobalController {

    @Operation(summary = "health_check", description = "health_check")
    @GetMapping("/health_check")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

}
