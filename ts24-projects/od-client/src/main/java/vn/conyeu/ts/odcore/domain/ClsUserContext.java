package vn.conyeu.ts.odcore.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "uid", callSuper = false)
public class ClsUserContext extends ClsModel<ClsUserContext> {
    private String lang;
    private String tz;
    private Long uid;
    private Boolean bin_size;

    public static ClsUserContext fix(Long uid, ClsUserContext context) {
        context = context == null ? new ClsUserContext() : context;
        if(context.tz == null)context.tz = "Asia/Ho_Chi_Minh";
        if(context.lang == null) context.lang = "vi_VN";
        if(context.uid == null) context.uid = uid;
        if(context.bin_size == null) context.bin_size = true;
        return context;
    }

    /**
     * Returns the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * Set the lang
     *
     * @param lang the value
     */
    public ClsUserContext setLang(String lang) {
        this.lang = lang;
        return this;
    }

    /**
     * Returns the tz
     */
    public String getTz() {
        return tz;
    }

    /**
     * Set the tz
     *
     * @param tz the value
     */
    public ClsUserContext setTz(String tz) {
        this.tz = tz;
        return this;
    }

    /**
     * Returns the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * Set the uid
     *
     * @param uid the value
     */
    public ClsUserContext setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    /**
     * Returns the bin_size
     */
    public Boolean getBin_size() {
        return bin_size;
    }

    /**
     * Set the bin_size
     *
     * @param bin_size the value
     */
    public ClsUserContext setBin_size(Boolean bin_size) {
        this.bin_size = bin_size;
        return this;
    }

}