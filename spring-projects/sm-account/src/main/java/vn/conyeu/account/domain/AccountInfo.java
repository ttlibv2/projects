package vn.conyeu.account.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongIdDate;

import java.time.LocalDate;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
//@formatter:on
public class AccountInfo extends LongIdDate<AccountInfo> {

    @MapsId @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JsonProperty("photo_url")
    private String picture;

    @Column(length = 50)
    private String identity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @JsonProperty("address_id")
    private Long addressId;

    @Column(length = 500)
    private String bio;

    @Column(length = 150)
    private String slogan;

    @JsonProperty("ext_url")
    private String extUrl;

    @Column(length = 100)
    private String education;

    @JsonProperty("user_id")
    public Long getId() {
        return super.getId();
    }



}