package OOTWhongik.OOTW.domain.member.domain;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.outfit.domain.Outfit;
import OOTWhongik.OOTW.global.common.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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
    private List<Clothes> clothesList = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "owner")
    private List<Outfit> outfitList = new ArrayList<>();


    public String getRoleKey() {
        return this.role.getKey();
    }
}
