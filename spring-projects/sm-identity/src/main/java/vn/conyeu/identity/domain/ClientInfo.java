package vn.conyeu.identity.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "infoId"))
//@formatter:on
public class ClientInfo extends LongUId<ClientInfo> {

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    private String redirectUri;
    private String scopes;
    private String clientName;

    @ColumnDefault("1")
    private Boolean active;

}