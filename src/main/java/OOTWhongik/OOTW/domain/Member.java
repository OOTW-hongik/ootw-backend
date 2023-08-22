package OOTWhongik.OOTW.domain;

import OOTWhongik.OOTW.domain.enumpack.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private List<Clothes> clothesList = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "owner")
    private List<Outfit> outfitList = new ArrayList<>();

    public Member update(String name) {
        this.name = name;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
