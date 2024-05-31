package vn.conyeu.ts.dtocls;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;

import java.io.Serializable;

public class ApiConfigDto implements Serializable {
    private Long apiId;
    private String apiCode;
    private String apiTitle;
    private String baseUrl;
    private String loginPath;
    private ObjectMap headers;
    private ObjectMap queries;
    private String username;
    private String password;
    private ObjectMap userInfo;

    public static ApiConfigDto from(UserApi info) {
        ApiConfigDto dto = from(info.getConfig());
        dto.setUpdateUser(info.getUserName());
        dto.setUpdateSecret(info.getSecret());
        dto.getHeaders().putAll(info.getHeaders());
        dto.getQueries().putAll(info.getQueries());
        return dto;
    }

    public static ApiConfigDto from(ApiInfo config) {
        ApiConfigDto dto = new ApiConfigDto();
        dto.setApiId(config.getId());
        dto.setApiCode(config.getCode());
        dto.setApiTitle(config.getTitle());
        dto.setBaseUrl(config.getBaseUrl());
        dto.setLoginPath(config.getLoginPath());
        dto.setUsername(config.getDefaultUser());
        dto.setPassword(config.getDefaultSecret());
        dto.getHeaders().putAll(config.getDefaultHeader());
        dto.getQueries().putAll(config.getDefaultQuery());
        return dto;
    }

    /**
     * Returns the apiId
     */
    public Long getApiId() {
        return apiId;
    }

    /**
     * Set the apiId
     *
     * @param apiId the value
     */
    public ApiConfigDto setApiId(Long apiId) {
        this.apiId = apiId;
        return this;
    }

    /**
     * Returns the apiCode
     */
    public String getApiCode() {
        return apiCode;
    }

    /**
     * Set the apiCode
     *
     * @param apiCode the value
     */
    public ApiConfigDto setApiCode(String apiCode) {
        this.apiCode = apiCode;
        return this;
    }

    /**
     * Returns the apiTitle
     */
    public String getApiTitle() {
        return apiTitle;
    }

    /**
     * Set the apiTitle
     *
     * @param apiTitle the value
     */
    public ApiConfigDto setApiTitle(String apiTitle) {
        this.apiTitle = apiTitle;
        return this;
    }

    /**
     * Returns the baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Set the baseUrl
     *
     * @param baseUrl the value
     */
    public ApiConfigDto setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * Returns the loginPath
     */
    public String getLoginPath() {
        return loginPath;
    }

    /**
     * Set the loginPath
     *
     * @param loginPath the value
     */
    public ApiConfigDto setLoginPath(String loginPath) {
        this.loginPath = loginPath;
        return this;
    }

    /**
     * Returns the headers
     */
    public ObjectMap getHeaders() {
        headers = ObjectMap.ifNull(headers);
        return headers;
    }

    /**
     * Set the headers
     *
     * @param headers the value
     */
    public ApiConfigDto setHeaders(ObjectMap headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Returns the queries
     */
    public ObjectMap getQueries() {
        queries = ObjectMap.ifNull(queries);
        return queries;
    }

    /**
     * Set the queries
     *
     * @param queries the value
     */
    public ApiConfigDto setQueries(ObjectMap queries) {
        this.queries = queries;
        return this;
    }

    /**
     * Returns the defaultUser
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the defaultUser
     *
     * @param username the value
     */
    public ApiConfigDto setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Returns the defaultSecret
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the defaultSecret
     *
     * @param password the value
     */
    public ApiConfigDto setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Returns the userInfo
     */
    public ObjectMap getUserInfo() {
        return userInfo;
    }

    /**
     * Set the userInfo
     *
     * @param userInfo the value
     */
    public ApiConfigDto setUserInfo(ObjectMap userInfo) {
        this.userInfo = userInfo;
        return this;
    }


    public void setUpdateUser(String userName) {
        if(Objects.notBlank(userName)) {
            setUsername(userName);
        }
    }

    public void setUpdateSecret(String secret) {
        if(Objects.notBlank(secret)) {
            setPassword(secret);
        }
    }
}