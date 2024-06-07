package vn.conyeu.address.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "addressId"))
//@formatter:on
public class Address extends LongUId<Address> {

    @Column(length = 100, nullable = false)
    private String targetId;

    @Column(length = 200)
    private String street1;

    @Column(length = 200)
    private String street2;

    @Column(length = 10)
    private String zipcode;

    private Double latitude;

    private Double longitude;

    @Column(length = 50)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provinceId")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "districtId")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wardId")
    private Ward ward;

}