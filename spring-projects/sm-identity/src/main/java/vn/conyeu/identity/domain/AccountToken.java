package vn.conyeu.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.domain.LongUIdDate;
import vn.conyeu.commons.beans.ObjectMap;

//@formatter:off
@Entity @Table
@Getter() @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "tokenId"))
//@formatter:on
public class AccountToken extends LongUIdDate<AccountToken> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @Column(length = 50, nullable = false, unique = true)
    private String token;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap device;

}