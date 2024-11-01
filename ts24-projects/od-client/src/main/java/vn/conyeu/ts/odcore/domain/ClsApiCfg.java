package vn.conyeu.ts.odcore.domain;

import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.service.OdClient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class ClsApiCfg implements Serializable, Cloneable {
    private String appUID;
    private String appName;
    private String title;
    private String baseUrl;
    private String loginPath;
    private ObjectMap headers;
    private ObjectMap queries;
    private Long accountId;
    private Boolean autoLogin;
    private ObjectMap cfgMenuLinks;

    //credential
    private String cookieValue;
    private String csrfToken;
    private String username;
    private String password;
    private ClsUser clsUser;
    private boolean saveLog;

    /**
     * Return true if info valid
     */
    public boolean hasLogin() {
        return Objects.allNotNull(clsUser, getCookie(), getCsrfToken(), getContext());
    }

    /**
     * Returns cookie for user
     */
    public String getCookie() {
        return clsUser == null ? cookieValue : clsUser.getCookie();
    }

    /**
     * Returns csrf_token for user
     */
    public String getCsrfToken() {
        return clsUser == null ? csrfToken : clsUser.getCsrfToken();
    }

    /**
     * Returns context for user
     */
    public ClsUserContext getContext() {
        return clsUser == null ? null : clsUser.getContext();
    }

    /** Returns the saveLog */
    public boolean isSaveLog() {
        return saveLog;
    }

    /**
     * Returns the clsUser
     */
    public ClsUser getClsUser() {
        checkUserLogin();
        return clsUser;
    }

    /**
     * Set the saveLog
     * @param saveLog the value
     */
    public ClsApiCfg setSaveLog(boolean saveLog) {
        this.saveLog = saveLog;
        return this;
    }

    /**
     * Set the menuLinks
     *
     * @param cfgMenuLinks the value
     */
    public ClsApiCfg setCfgMenuLinks(ObjectMap cfgMenuLinks) {
        this.cfgMenuLinks = cfgMenuLinks;
        return this;
    }

    /**
     * Set the appName
     *
     * @param appName the value
     */
    public ClsApiCfg setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    /**
     * Set the accountId
     *
     * @param accountId the value
     */
    public ClsApiCfg setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * Set the appName
     *
     * @param appUID the value
     */
    public ClsApiCfg setAppUID(String appUID) {
        this.appUID = appUID;
        return this;
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
     * Set the headers
     *
     * @param headers the value
     */
    public ClsApiCfg setHeaders(ObjectMap headers) {
        this.headers = headers;
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
     * Set the queries
     *
     * @param queries the value
     */
    public ClsApiCfg setQueries(ObjectMap queries) {
        this.queries = queries;
        return this;
    }

    /**
     * Set the title
     *
     * @param title the value
     */
    public ClsApiCfg setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Set the autoLogin
     *
     * @param autoLogin the value
     */
    public ClsApiCfg setAutoLogin(Boolean autoLogin) {
        this.autoLogin = autoLogin;
        return this;
    }

    /**
     * Set the clsUser
     *
     * @param clsUser the value
     */
    public ClsApiCfg setClsUser(ClsUser clsUser) {
        this.clsUser = clsUser;
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
     * Set the password
     *
     * @param password the value
     */
    public ClsApiCfg setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Set the username
     *
     * @param username the value
     */
    public ClsApiCfg setUsername(String username) {
        this.username = username;
        return this;
    }

    public void checkUserLogin() {
        if (!hasLogin()) {
            throw OdClient.notLogin(appName);
        }
    }

    public Errors validate(SmartValidator validator) {
        return validator.validateObject(this);
    }

    public void validateBaseUrl() {
        Map<String, String> params = checkValidateBase();
        throwParamsError(params);
    }

    public void validateLogin() {
        Map<String, String> params = checkValidateBase();
        if(Objects.isBlank(username)) params.put("user_name", "api.user-name.notBlank");
        if(Objects.isBlank(password)) params.put("password", "api.password.notBlank");
        throwParamsError(params);
    }

    @Override
    protected ClsApiCfg clone() {
        try {
            ClsApiCfg cfg = (ClsApiCfg) super.clone();
            cfg.headers = ObjectMap.clone(headers);
            cfg.queries = ObjectMap.clone(queries);
            cfg.clsUser = clsUser == null ? null : clsUser.clone();
            return cfg;
        } catch (CloneNotSupportedException exp) {
            throw new InternalError(exp);
        }
    }

    private Map<String, String> checkValidateBase() {
        Map<String, String> params = new HashMap<>();
        if (Objects.isBlank(baseUrl)) params.put("base_url", "api.base-url.notBlank");
        if (Objects.isBlank(loginPath)) params.put("login_path", "api.login-path.notBlank");
        if (Objects.isBlank(appUID)) params.put("app_name", "api.app-name.notBlank");
        if (Objects.isBlank(title)) params.put("app_title", "api.app-title.notBlank");
        if (Objects.isBlank(appName)) params.put("service_name", "api.service-name.notBlank");
        if (Objects.isNull(accountId)) params.put("account_id", "api.account-id.notNull");
        return params;
    }

    private void throwParamsError(Map<String, String> params) {
        if (!params.isEmpty()) throw BaseException.e400("e400")
                .message("Vui lòng điền đầy đủ thông tin.")
                .arguments("fields", params);
    }

    public void update(ClsApiCfg config) {
        MapperHelper.update(this, config);
        validateBaseUrl();
    }
}