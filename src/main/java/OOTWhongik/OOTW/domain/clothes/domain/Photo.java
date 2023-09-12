package OOTWhongik.OOTW.domain.clothes.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

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
