package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.conyeu.ts.odcore.domain.ClsModel;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClsNameSearchOption extends ClsModel {
    private Integer limit;
    private String url;
    private Object args;

    public static ClsNameSearchOption create() {
        return new ClsNameSearchOption();
    }

    public static ClsNameSearchOption fixDefault(ClsNameSearchOption option) {
        if(option == null) option = new ClsNameSearchOption();
        if(option.limit == null) option.limit = 80;
        return option;
    }

    public static ClsNameSearchOption withArgs(Object object) {
        return new ClsNameSearchOption().setArgs(new Object[]{object});
    }

    /**
     * Returns the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Set the limit
     *
     * @param limit the value
     */
    public ClsNameSearchOption setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Returns the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the url
     *
     * @param url the value
     */
    public ClsNameSearchOption setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Returns the args
     */
    public Object getArgs() {
        return args;
    }

    /**
     * Set the args
     *
     * @param args the value
     */
    public ClsNameSearchOption setArgs(Object args) {
        this.args = args;
        return this;
    }
}