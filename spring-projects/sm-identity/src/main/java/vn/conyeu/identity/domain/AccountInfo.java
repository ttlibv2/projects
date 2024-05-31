package vn.conyeu.identity.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ListStringToString;
import vn.conyeu.common.domain.LongIdDate;
import vn.conyeu.common.domain.StringId;
import vn.conyeu.common.domain.StringIdDate;

import java.time.LocalDate;
import java.util.List;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "accountId"))
//@formatter:on
public class AccountInfo extends LongIdDate<AccountInfo> {

    @MapsId @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @Column(length = 50)
    @JsonProperty("first_name")
    private String firstName;

    @Column(length = 50)
    @JsonProperty("last_name")
    private String lastName;

    @Column(length = 10)
    private String gender;

    @Column(length = 100)
    private String avatar;

    @Column(length = 100)
    @JsonProperty("cover")
    private String coverImg;

    @Column(length = 500)
    private String bio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Column(length = 100)
    @JsonProperty("nick_name")
    private String nickName;

    @Column(length = 300)
    private String slogan;

    @Column(length = 50)
    private String identity;

    @Column(length = 100)
    private String education;

    @Convert(converter = ListStringToString.class)
    @Column(length = 300)
    private List<String> languages;

    @Column(length = 20)
    private Long addressId;

    public void setAccount(Account account) {
        this.account = account;
        if(account !=null) {
            this.account.setInfo(this);
        }
    }
}