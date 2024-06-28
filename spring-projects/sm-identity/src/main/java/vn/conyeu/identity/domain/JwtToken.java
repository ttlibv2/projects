package vn.conyeu.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUIdDate;

//@formatter:off
@Entity @Table
@Getter() @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "tokenId"))
//@formatter:on
public class JwtToken extends LongUIdDate<JwtToken> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @Column(length = 1000, nullable = false)
    private String bearerToken;

    @Column(length = 50, nullable = false)
    private String ipAddress;
}