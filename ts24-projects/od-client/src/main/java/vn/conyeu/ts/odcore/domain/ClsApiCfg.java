package vn.conyeu.ts.odcore.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.Unauthorized;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.restclient.ClientBuilder;
import vn.conyeu.ts.odcore.service.OdClient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClsApiCfg implements Serializable, Cloneable {
    private String apiCode;
    private String apiTitle;
    private String baseUrl;
    private String loginPath;
    private ObjectMap headers;
    private ObjectMap queries;
    private String cookieValue;
    private String csrfToken;
    private String userName;
    private String password;
    private ClsUser clsUser;
    private boolean autoLogin = true;
    private int maxTryLogin = 1;

    private Consumer<ClientBuilder> customBuilderConsumer;

    public void validate() {
        Map<String, String> params = new HashMap<>();
        if(Objects.isBlank(baseUrl)) params.put("baseUrl", "");
        if(Objects.isBlank(loginPath)) params.put("loginPath", "");
        if(!params.isEmpty()) throw BaseException.e400("e400")
                .message("Vui lòng điền đầy đủ thông tin.")
                .arguments("fields", params);
    }

    public boolean isLogin() {
        return cookieValue != null && clsUser != null;
    }


    /**
     * Set the baseUrl
     *
     * @param baseUrl the value
     */
    public ClsApiCfg setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }


    /**
     * Set the loginPath
     *
     * @param loginPath the value
     */
    public ClsApiCfg setLoginPath(String loginPath) {
        this.loginPath = loginPath;
        return this;
    }

    /**
     * Set the cookieValue
     *
     * @param cookieValue the value
     */
    public ClsApiCfg setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
        return this;
    }


    /**
     * Set the csrfToken
     *
     * @param csrfToken the value
     */
    public ClsApiCfg setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    /**
     * Returns the requestHeader
     */
    public ObjectMap getHeaders() {
        headers = ObjectMap.ifNull(headers);
        return headers;
    }

    /**
     * Set the requestHeader
     *
     * @param headers the value
     */
    public ClsApiCfg setHeaders(ObjectMap headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Returns the requestQuery
     */
    public ObjectMap getQueries() {
       queries = ObjectMap.ifNull(headers);
        return queries;
    }

    /**
     * Set the requestQuery
     *
     * @param queries the value
     */
    public ClsApiCfg setQueries(ObjectMap queries) {
        this.queries = queries;
        return this;
    }


    /**
     * Set the userName
     *
     * @param userName the value
     */
    public ClsApiCfg setUserName(String userName) {
        this.userName = userName;
        return this;
    }


    /**
     * Set the secret
     *
     * @param password the value
     */
    public ClsApiCfg setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Returns the userContext
     */
    public ClsUserContext getUserContext() {
        Asserts.notNull(clsUser, "User not login");
        return clsUser.getContext();
    }

    /**
     * Returns the userId
     */
    public Long getUserId() {
        //Asserts.notNull(clsUser, "User not login");
        return clsUser == null ? null : clsUser.getId();
    }

    /**
     * Set the clsUser
     *
     * @param clsUser the value
     */
    public ClsApiCfg setClsUser(ClsUser clsUser) {
        this.clsUser = clsUser;
        //this.cookieValue = clsUser.getCookie();
       // this.csrfToken = clsUser.getCsrfToken();
        return this;
    }

    /**
     * Set the autoLogin
     *
     * @param autoLogin the value
     */
    public ClsApiCfg setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
        return this;
    }

    /**
     * Set the apiCode
     *
     * @param apiCode the value
     */
    public ClsApiCfg setApiCode(String apiCode) {
        this.apiCode = apiCode;
        return this;
    }

    /**
     * Set the apiTitle
     *
     * @param apiTitle the value
     */
    public ClsApiCfg setApiTitle(String apiTitle) {
        this.apiTitle = apiTitle;
        return this;
    }


    /**
     * Set the customBuilderConsumer
     *
     * @param customBuilderConsumer the value
     */
    public ClsApiCfg setCustomBuilderConsumer(Consumer<ClientBuilder> customBuilderConsumer) {
        this.customBuilderConsumer = customBuilderConsumer;
        return this;
    }

    @Override
    public ClsApiCfg clone()  {
        return new ClsApiCfg()
                .setBaseUrl(baseUrl)
                .setLoginPath(loginPath)
                .setClsUser(clsUser)
                .setCookieValue(cookieValue)
                .setCsrfToken(csrfToken)
                .setApiTitle(apiTitle)
                .setApiTitle(apiTitle)
                .setHeaders(getHeaders().copy())
                .setQueries(getQueries().copy())
                .setCustomBuilderConsumer(customBuilderConsumer);
    }

    public void updateFrom(ClsApiCfg config) {
        //MapperHelper.update(this, config);
        ClsApiCfg self = this;
        set(config.getBaseUrl(), self::setBaseUrl);
        set(config.getLoginPath(), self::setLoginPath);
        set(config.getClsUser(), self::setClsUser);
        set(config.getCsrfToken(), self::setCsrfToken);
        set(config.getApiTitle(), self::setApiTitle);
        set(config.getHeaders(), self::setHeaders);
        set(config.getQueries(), self::setQueries);
        set(config.getCustomBuilderConsumer(), self::setCustomBuilderConsumer);
    }

    public <T> void set(T value, Consumer<T> consumer) {
        if(value != null) consumer.accept(value);
    }

    void set(ObjectMap value, Consumer<ObjectMap> consumer) {
        if(value != null && !value.isEmpty()) {
            consumer.accept(value.copy());
        }
    }

    public void checkUserLogin() {
        if(clsUser == null) {
           throw  OdClient.notLogin(apiCode);
        }
    }
}