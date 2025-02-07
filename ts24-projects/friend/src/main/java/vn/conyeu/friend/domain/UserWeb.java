package vn.conyeu.friend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ListStringToString;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.domain.LongIdDate;
import vn.conyeu.commons.beans.ObjectMap;

import java.util.ArrayList;
import java.util.List;

//@formatter:off
@Entity @Table(name = "friendUserWeb")
@Getter @Setter @NoArgsConstructor @DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "userid"))
//@formatter:on
public class UserWeb extends LongIdDate<UserWeb> {

    @Column(length = 50, unique = true, nullable = false)
    private String webId; // domain::webID

    @Column(length = 350)
    private String address;

    @Column(length = 100)
    private String province;

    @Column(length = 100)
    private String district;

    @Column(length = 15)
    private String gender;

    @Column(length = 15)
    private String name;

    @Column(length = 15)
    private String phone;

    @Column(length = 350)
    private String avatar;

    @Column(length = 500)
    private String bio;

    @Column(length = 25)
    private String dob;

    @Column(length = 25)
    private String lastAt;

    @Column(length = 10)
    private String state;

    @Column(columnDefinition = "json")
    @Convert(converter = ListStringToString.class)
    private List<String> images;

    @Column(columnDefinition = "json")
    @Convert(converter = MapString.class)
    private ObjectMap info;


    public List<String> getImages() {
        if(images == null) images = new ArrayList<>();
        return images;
    }

    public void updateWebId(String domain, Object webId) {
        this.webId = domain + "::" + webId.toString();
    }

}