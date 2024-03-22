package OOTWhongik.OOTW.member.domain;

import OOTWhongik.OOTW.clothes.domain.Clothes;
import OOTWhongik.OOTW.outfit.domain.Outfit;
import OOTWhongik.OOTW.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private String name;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Location location;
    @Embedded
    private OauthId oauthId;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Clothes> clothesList;

    @OneToMany(mappedBy = "owner")
    private List<Outfit> outfitList;

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean contains(Clothes clothes) {
        return this.clothesList.contains(clothes);
    }

    public boolean isOwner(Outfit outfit) {
        return this.outfitList.contains(outfit);
    }
}
