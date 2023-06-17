package OOTWhongik.OOTW.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column
    private String name;
    @Column(unique = true)
    private String email;
    @Column
    private String location;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @JsonBackReference
    @OneToMany(mappedBy = "member")
    private List<Clothes> clothesList;

    @JsonBackReference
    @OneToMany(mappedBy = "owner")
    private List<Outfit> outfitList;

    public Member update(String name) {
        this.name = name;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
