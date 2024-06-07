package vn.conyeu.identity.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ListStringToString;
import vn.conyeu.common.domain.LongUIdDate;

import java.time.LocalDate;
import java.util.List;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"account"})
@AttributeOverride(name = "id", column = @Column(name = "accountId"))
//@formatter:on
public class AccountInfo extends LongUIdDate<AccountInfo> {

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
        if(account != null) {
            account.setInfo(this);
        }
    }

    public String getFullName() {
        if(firstName != null && lastName != null) return firstName + " " + lastName;
        else if(firstName == null) return lastName;
        else return firstName;
    }
}