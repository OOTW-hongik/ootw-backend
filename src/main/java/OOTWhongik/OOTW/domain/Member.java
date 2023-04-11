package OOTWhongik.OOTW.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String location;

    @OneToMany(mappedBy = "member")
    private List<Clothes> clothesList;
}
