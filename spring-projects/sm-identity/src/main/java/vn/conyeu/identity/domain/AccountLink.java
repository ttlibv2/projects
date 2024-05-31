package vn.conyeu.identity.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ObjectMapToString;
import vn.conyeu.common.domain.LongIdDate;
import vn.conyeu.commons.beans.ObjectMap;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"account", "device"})
@AttributeOverride(name = "id", column = @Column(name = "accountLink"))
//@formatter:on
public class AccountLink extends LongIdDate<AccountLink> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accountId", nullable = false)
    private Account account;

    @Column(length = 100, nullable = false)
    private String providerType;

    @Column(length = 150, nullable = false)
    private String externalId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "deviceId")
    private Device device;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap custom;

    @JsonAnySetter
    public void set(String field, Object data) {
        getCustom().set(field, data);
    }

    @JsonAnyGetter
    public ObjectMap getCustom() {
        custom = ObjectMap.ifNull(custom);
        return custom;
    }
}