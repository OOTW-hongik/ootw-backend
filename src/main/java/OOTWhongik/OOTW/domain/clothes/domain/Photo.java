package OOTWhongik.OOTW.domain.clothes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @NotEmpty
    private String originalFileName;

    @NotEmpty
    private String storedFilePath;

    private Long fileSize;

    public void update(String originalFilename, String storedFilePath, Long fileSize) {
        this.originalFileName = originalFilename;
        this.storedFilePath = storedFilePath;
        this.fileSize = fileSize;
    }
}
