package vn.conyeu.ts.odcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClsUser extends ClsModel<ClsUser> {
    private  String display_name;
    private  String tz_offset;
    private  Object company_id;
    private Object[] parent_id;// [id,name]
    private  String signature;
    private  String user_name;
    private  String passwd;
    private Long id;
    private String name;
    private String email;
    private Long partner_id;
    private Boolean selected;
    private ObjectMap cache_hashes;

    @JsonProperty("menu_links")
    private ObjectMap menuLinks;
    private ClsUserContext context;
    private Object tsUser;

    @JsonProperty("csrf_token")
    private String csrfToken;

    private String cookie;

    //
    private Long company_uid;
    private String company_name;

    /**
     * Set the display_name
     *
     * @param display_name the value
     */
    public ClsUser setDisplay_name(String display_name) {
        this.display_name = display_name;
        return this;
    }

    /**
     * Set the tz_offset
     *
     * @param tz_offset the value
     */
    public ClsUser setTz_offset(String tz_offset) {
        this.tz_offset = tz_offset;
        return this;
    }

    public Long getCompany_Uid() {
        return getObjectID(company_id);
    }


    /**
     * Set the company_id
     *
     * @param company_id the value
     */
    public ClsUser setCompany_id(Object company_id) {
        this.company_id = company_id;
        return this;
    }

    /**
     * Set the parent_id
     *
     * @param parent_id the value
     */
    public ClsUser setParent_id(Object[] parent_id) {
        this.parent_id = parent_id;
        return this;
    }

    /**
     * Set the signature
     *
     * @param signature the value
     */
    public ClsUser setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    /**
     * Set the user_name
     *
     * @param user_name the value
     */
    public ClsUser setUser_name(String user_name) {
        this.user_name = user_name;
        return this;
    }

    /**
     * Set the passwd
     *
     * @param passwd the value
     */
    public ClsUser setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    /**
     * Set the id
     *
     * @param id the value
     */
    public ClsUser setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Set the email
     *
     * @param email the value
     */
    public ClsUser setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Set the partner_id
     *
     * @param partner_id the value
     */
    public ClsUser setPartner_id(Long partner_id) {
        this.partner_id = partner_id;
        return this;
    }

    /**
     * Set the selected
     *
     * @param selected the value
     */
    public ClsUser setSelected(Boolean selected) {
        this.selected = selected;
        return this;
    }

    /**
     * Set the cache_hashes
     *
     * @param cache_hashes the value
     */
    public ClsUser setCache_hashes(ObjectMap cache_hashes) {
        this.cache_hashes = cache_hashes;
        return this;
    }

    /**
     * Set the menuLinks
     *
     * @param menuLinks the value
     */
    public ClsUser setMenuLinks(ObjectMap menuLinks) {
        this.menuLinks = menuLinks;
        return this;
    }

    public String getMenuId() {
        ObjectMap cache = getCache_hashes();
        return cache == null ? null : cache.getString("load_menus");
    }

    /**
     * Set the context
     *
     * @param context the value
     */
    public ClsUser setContext(ClsUserContext context) {
        this.context = context;
        return this;
    }

    /**
     * Set the csrfToken
     *
     * @param csrfToken the value
     */
    public ClsUser setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    /**
     * Set the cookie
     *
     * @param cookie the value
     */
    public ClsUser setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    public static ClsUser of(ClsUser object) {
        ClsUser user = new ClsUser();
        BeanUtils.copyProperties(object, user);
        return user;
    }

    @Override
    public ObjectMap cloneMap() {
        return super.cloneMap();
    }

    public static ClsUser from(ObjectMap obj) {
        ClsUser cls = obj.asObject(ClsUser.class);

        Object[] objects = Objects.toObjectArray(cls.company_id);
        if(objects != null && objects.length > 0) {
            cls.set("comp_id", objects[0]);
            cls.set("comp_name", objects[1]);
        }

        return cls;
    }

    public ClsUser setName(String name) {
        this.name = name;
        this.setDisplay_name(name);
        return this;
    }


    @Override
    public String toString() {
        return getDisplay_name();
    }

    /**
     * Set the tsUser
     *
     * @param tsUser the value
     */
    public ClsUser setTsUser(Object tsUser) {
        this.tsUser = tsUser;
        return this;
    }
}