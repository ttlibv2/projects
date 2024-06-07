package vn.conyeu.address.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;


//@formatter:off
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@MappedSuperclass
//@formatter:on
public abstract class BaseAddress<E extends BaseAddress<E>> extends LongUId<E> {

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

    @Column(length = 20)
    private Long countryId;

    @Column(length = 20)
    private Long provinceId;

    @Column(length = 20)
    private Long districtId;

    @Column(length = 20)
    private Long wardId;

    @Column(length = 50)
    private String countryText;

    @Column(length = 50)
    private String provinceText;

    @Column(length = 50)
    private String districtText;

    @Column(length = 50)
    private String wardText;

}