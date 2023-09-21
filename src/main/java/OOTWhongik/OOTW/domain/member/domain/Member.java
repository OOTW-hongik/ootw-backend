package OOTWhongik.OOTW.domain.member.domain;

import OOTWhongik.OOTW.domain.clothes.domain.Clothes;
import OOTWhongik.OOTW.domain.outfit.domain.Outfit;
import OOTWhongik.OOTW.global.common.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(uniqueConstraints = {
                @UniqueConstraint(
                        name = "oauth_id_unique",
                        columnNames = {
                                "oauth_server_id",
                                "oauth_server"
                        }
                ),
        }
)
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
    @Embedded
    private OauthId oauthId;

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

    public void setLocation(String location) {
        this.location = location;
    }
}
