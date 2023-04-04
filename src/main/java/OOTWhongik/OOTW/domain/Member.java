package OOTWhongik.OOTW.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @Column
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;
}
